package com.letsellify.logistics.components.logistics.core.agentManagement;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.communication.core.emailManagement.EmailService;
import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.logistics.core.agentManagement.data.Agent;
import com.letsellify.logistics.components.logistics.core.agentManagement.data.AgentInfo;
import com.letsellify.logistics.components.logistics.core.agentManagement.data.AgentProfileInfo;
import com.letsellify.logistics.components.logistics.core.agentManagement.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistics.core.agentManagement.database.repository.AgentRepository;
import com.letsellify.logistics.components.logistics.core.agentManagement.event.AgentAwaitApproval;
import com.letsellify.logistics.components.logistics.core.agentManagement.event.AgentNameUpdateEvent;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.AgentApprovedException;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.AgentExistsException;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.NoSuchAgentException;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.UnapprovedAgentException;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto.AgentContactInfoDto;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto.AgentPersonalInfoDto;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto.StoreDetailDto;
import com.letsellify.logistics.components.logistics.core.kycManagement.KycManager;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.KycDocumentType;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.LogisticKycDocument;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.LogisticKycs;
import com.letsellify.logistics.components.logistics.core.kycManagement.exception.NoKycRecordFoundException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.NigeriaStatesManager;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.event.LogisticRequestBroadcast;
import com.letsellify.logistics.components.user.core.userManagement.event.UserOfRoleAgentCreated;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:00
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class AgentManager {
    private final AgentRepository agentRepository;
    private final KycManager kycManager;
    private final NigeriaStatesManager nigeriaStatesManager;
    private final ApplicationEventPublisher eventPublisher;
    private final FileStorageManager fileStorageManager;
    private final EmailService emailService;


    @Async
    @EventListener
    @Transactional
    public void on(final UserOfRoleAgentCreated event) throws AgentExistsException {
        log.info("Handling AgentCreatedEvent for email: {}", event.getUserEmail());
        // Agent-specific logic here, e.g., assigning delivery zones
        final String agentEmail = event.getUserEmail();
        final String agentName = event.getName();
        if (this.agentRepository.existsByEmail(agentEmail)) {
            throw new AgentExistsException("Agent with email " + agentEmail + " all ready exists");
        }
        final AgentEntity entity = new AgentEntity(agentEmail, agentName);
        this.agentRepository.save(entity);
    }


    @EventListener
    public void on(final LogisticRequestBroadcast broadcast) {
        log.info("we have got a shipping request order from: {}", broadcast.getSenderId());
        final List<AgentEntity> agentEntityList = this.agentRepository.findByApprovedAndAcceptingStorage(true, true);
        final Set<Agent> agents = new HashSet<>();
        for (final AgentEntity entity : agentEntityList) {
            agents.add(new Agent(entity));
        }
        // send email
        this.emailService.sendLogisticRequestedMailToAgents(broadcast, agents);
    }

    @Transactional
    public AgentInfo viewAgentPersonalInfo(final @NonNull String agentEmail) throws NoSuchAgentException, NoKycRecordFoundException {
        final AgentEntity entity = this.agentRepository.findByEmail(agentEmail)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + agentEmail + " found"));
        final LogisticKycs kyc = this.kycManager.findAgentKyc(agentEmail);
        return new AgentInfo(entity, this.getKycsPresignedUrl(kyc.getKycs()));

    }

    @Transactional
    public Agent approveAgent(final @NonNull String email) throws NoSuchAgentException, AgentApprovedException, NoKycRecordFoundException {
        final AgentEntity entity = this.agentRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + email + " found"));
        if (entity.isApproved()) {
            throw new AgentApprovedException("Agent with email " + email + " all ready approved");
        }
        this.kycManager.approveKyc(entity.getKycId());
        entity.approve();
        this.agentRepository.save(entity);
        this.emailService.sendApprovalSuccessMail(email, LogisticAppRole.AGENT);
        return new Agent(entity);
    }

    //internal method
    public Agent findAgent(final @NonNull String agentUsername) throws NoSuchAgentException {
        final AgentEntity entity = this.agentRepository.findByEmail(agentUsername)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + agentUsername + " found"));
        return new Agent(entity);
    }

    //internal method
    public Agent findAgent(final @NonNull UUID agentId) throws NoSuchAgentException {
        final AgentEntity entity = this.agentRepository.findById(agentId)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with id " + agentId + " found"));
        return new Agent(entity);
    }

    AgentInfo viewPersonalInfoAfterApproval(final @NonNull String agentEmail) throws NoSuchAgentException, NoKycRecordFoundException {
        final AgentEntity entity = this.agentRepository.findByEmail(agentEmail)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + agentEmail + " found"));
        final LogisticKycs kyc = this.kycManager.findKyc(entity.getId());
        return new AgentInfo(entity, this.getKycsPresignedUrl(kyc.getKycs()));
    }

    @Transactional
    LogisticKycDocument uploadKycDocument(final @NonNull String userEmail, final @NonNull KycDocumentType kycDocumentType, final @NonNull MultipartFile multipartFile) throws NoSuchAgentException, IOException, AgentApprovedException {
        final AgentEntity entity = this.agentRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + userEmail + " found"));
        if (entity.isApproved()) {
            throw new AgentApprovedException("Agent with email " + userEmail + " all ready approved");
        }
        return this.kycManager.uploadKyc(entity.getEmail(), LogisticAppRole.AGENT, kycDocumentType, multipartFile);
    }

    @Transactional
    void deleteKyc(final @NonNull String userEmail, final @NonNull String kycId) throws NoSuchAgentException, AgentApprovedException, NoKycRecordFoundException {
        final AgentEntity entity = this.agentRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + userEmail + " found"));
        if (entity.isApproved()) {
            throw new AgentApprovedException("Agent with email " + userEmail + " all ready approved");
        }
        this.kycManager.deleteKyc(userEmail, kycId);

    }

    @Transactional
    public AgentProfileInfo setInfo(
            final @NonNull String email,
            final @NonNull AgentPersonalInfoDto personalInfoDto,
            final @NonNull AgentContactInfoDto contactInfoDto,
            final @NonNull StoreDetailDto storeDetailDto
    ) throws NoSuchAgentException, NoSuchStateException, IllegalLGAException {
        final AgentEntity entity = this.agentRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + email + " found"));

        final String agentNameBeforeUpdate = entity.getPersonalInfo().getName();
        if (this.nigeriaStatesManager.validateStateLga(personalInfoDto.state(), personalInfoDto.lga()) || this.nigeriaStatesManager.validateStateLga(storeDetailDto.state(), storeDetailDto.lga())) {
            throw new IllegalLGAException("LGA does not match state");
        }
        final AgentEntity.PersonalInfoEmbeddable personalInfoEmbeddable = new AgentEntity.PersonalInfoEmbeddable(personalInfoDto.name(), personalInfoDto.state(), personalInfoDto.lga(), personalInfoDto.address());
        final AgentEntity.ContactInfoEmbeddable contactInfoEmbeddable = new AgentEntity.ContactInfoEmbeddable(contactInfoDto.whatsAppPhone(), contactInfoDto.phone());
        final AgentEntity.StoreDetailEmbeddable storeDetailEmbeddable = new AgentEntity.StoreDetailEmbeddable(storeDetailDto.state(), storeDetailDto.lga(), storeDetailDto.address());
        entity.setPersonalInfo(personalInfoEmbeddable);
        entity.setContactInfo(contactInfoEmbeddable);
        entity.setStoreDetail(storeDetailEmbeddable);
        final String agentNameAfterUpdate = entity.getPersonalInfo().getName();
        if (personalInfoDto.name() != null && !agentNameBeforeUpdate.equals(entity.getPersonalInfo().getName())) {
            this.eventPublisher.publishEvent(new AgentNameUpdateEvent(entity.getEmail(), agentNameBeforeUpdate, agentNameAfterUpdate));
        }
        this.agentRepository.save(entity);
        return new AgentProfileInfo(entity);
    }

    public UUID getAgentId(final @NonNull String agentEmail) throws NoSuchAgentException {
        AgentEntity entity = this.agentRepository.findByEmail(agentEmail)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + agentEmail + " found"));
        return entity.getId();
    }

    @Transactional
    AgentInfo confirmInfoSubmissionForApproval(final @NonNull String email) throws NoSuchAgentException, AgentApprovedException, NoKycRecordFoundException {
        final AgentEntity entity = this.agentRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + email + " found"));
        if (entity.isApproved()) {
            throw new AgentApprovedException("Agent with email " + email + " all ready approved");
        }
        final LogisticKycs kyc = this.kycManager.findAgentKyc(email);
        entity.setKycId(kyc.getKycId());
        this.agentRepository.save(entity);
        final Map<KycDocumentType, String> kycs = this.getKycsPresignedUrl(kyc.getKycs());
        final AgentInfo agentInfo = new AgentInfo(entity, kycs);
        this.eventPublisher.publishEvent(new AgentAwaitApproval(agentInfo));
        return agentInfo;
    }


    @Transactional
    Agent setCurrentlyAcceptingStorage(final @NonNull String email, final boolean status) throws NoSuchAgentException, UnapprovedAgentException {
        final AgentEntity entity = this.agentRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + email + " found"));
        if (!entity.isApproved()) {
            throw new UnapprovedAgentException("You have not been approved");
        }
        entity.setAcceptingStorage(status);
        this.agentRepository.save(entity);
        return new Agent(entity);
    }


    private Map<KycDocumentType, String> getKycsPresignedUrl(final List<LogisticKycDocument> kycDocuments) {
        return kycDocuments
                .stream()
                .collect(Collectors
                        .toMap(
                                LogisticKycDocument::getKycDocument,
                                logisticsKyc -> this.fileStorageManager.generatePresignedUrl(logisticsKyc.getKycFilePath()
                                )
                        )
                );
    }


    public Agent findAndValidateAgent(@NonNull String userName) throws NoSuchAgentException {
        AgentEntity entity = this.agentRepository.findByEmail(userName)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + userName + " found"));
        this.validateAgent(entity);
        return new Agent(entity);
    }

    private void validateAgent(final @NonNull AgentEntity agentEntity) {

    }
}
