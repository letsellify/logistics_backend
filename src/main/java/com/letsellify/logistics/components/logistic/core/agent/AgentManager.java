package com.letsellify.logistics.components.logistic.core.agent;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.letsellify.logistics.components.logistic.core.dispatcher.exception.NoSuchDispatcherException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.communication.core.email.EmailService;
import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.logistic.core.agent.data.AgentInfo;
import com.letsellify.logistics.components.logistic.core.agent.data.LogisticAgent;
import com.letsellify.logistics.components.logistic.core.agent.data.LogisticAgentInfo;
import com.letsellify.logistics.components.logistic.core.agent.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistic.core.agent.database.repository.AgentRepository;
import com.letsellify.logistics.components.logistic.core.agent.event.AgentAwaitApproval;
import com.letsellify.logistics.components.logistic.core.agent.event.AgentNameUpdateEvent;
import com.letsellify.logistics.components.logistic.core.agent.exception.AgentApprovedException;
import com.letsellify.logistics.components.logistic.core.agent.exception.AgentExistsException;
import com.letsellify.logistics.components.logistic.core.agent.exception.NoSuchAgentException;
import com.letsellify.logistics.components.logistic.core.agent.exception.UnapprovedAgentException;
import com.letsellify.logistics.components.logistic.core.agent.rest.dto.AgentContactInfoDto;
import com.letsellify.logistics.components.logistic.core.agent.rest.dto.AgentPersonalInfoDto;
import com.letsellify.logistics.components.logistic.core.agent.rest.dto.StoreDetailDto;
import com.letsellify.logistics.components.logistic.core.kyc.KycManager;
import com.letsellify.logistics.components.logistic.core.kyc.data.KycDocumentType;
import com.letsellify.logistics.components.logistic.core.kyc.data.LogisticKycDocument;
import com.letsellify.logistics.components.logistic.core.kyc.data.LogisticKycs;
import com.letsellify.logistics.components.logistic.core.kyc.exception.NoKycRecordFoundException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.StateLGAManager;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistic.core.request.event.LogisticRequestBroadcast;
import com.letsellify.logistics.components.user.core.logisticUser.event.UserOfRoleAgentCreated;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final StateLGAManager stateLGAManager;
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
        final AgentEntity entity = new AgentEntity(agentEmail,agentName);
        this.agentRepository.save(entity);
    }


    @EventListener
    public void on(final LogisticRequestBroadcast broadcast) {
        log.info("we have got a shipping request order from: {}", broadcast.getSenderId());
        final List<AgentEntity> agentEntityList = this.agentRepository.findByApprovedAndAcceptingStorage(true,true);
        final Set<LogisticAgent> agents = new HashSet<>();
        for (final AgentEntity entity: agentEntityList) {
            agents.add(new LogisticAgent(entity));
        }
        // send email
        this.emailService.sendLogisticRequestedMailToAgents(broadcast, agents);
    }

    @Transactional
    public LogisticAgentInfo viewAgentPersonalInfo(final @NonNull String agentEmail) throws NoSuchAgentException, NoKycRecordFoundException {
        final AgentEntity entity = this.agentRepository.findByEmail(agentEmail)
                                                       .orElseThrow(()-> new NoSuchAgentException("No such agent with email " + agentEmail + " found"));
        final LogisticKycs kyc = this.kycManager.findAgentKyc(agentEmail);
        return new LogisticAgentInfo(entity, this.getKycsPresignedUrl(kyc.getKycs()));

    }

    @Transactional
    public LogisticAgent approveAgent(final @NonNull String email) throws NoSuchAgentException, AgentApprovedException, NoKycRecordFoundException {
        final AgentEntity entity = this.agentRepository.findByEmail(email)
                                                                 .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + email + " found"));
        if (entity.isApproved()) {
            throw new AgentApprovedException("Agent with email " + email + " all ready approved");
        }
        this.kycManager.approveKyc(entity.getKycId());
        entity.approve();
        this.agentRepository.save(entity);
        this.emailService.sendApprovalSuccessMail(email, LogisticAppRole.AGENT);
        return new LogisticAgent(entity);
    }

    //internal method
    public LogisticAgent findAgent(final @NonNull String agentUsername) throws NoSuchAgentException {
        final AgentEntity entity = this.agentRepository.findByEmail(agentUsername)
                                                                 .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + agentUsername + " found"));
        return new LogisticAgent(entity);
    }

    LogisticAgentInfo viewPersonalInfoAfterApproval(final @NonNull String agentEmail) throws NoSuchAgentException, NoKycRecordFoundException {
        final AgentEntity entity = this.agentRepository.findByEmail(agentEmail)
                                                                 .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + agentEmail + " found"));
        final LogisticKycs kyc = this.kycManager.findKyc(entity.getId());
        return new LogisticAgentInfo(entity,this.getKycsPresignedUrl(kyc.getKycs()));
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
    public AgentInfo setInfo(
      final @NonNull String email,
      final @NonNull AgentPersonalInfoDto personalInfoDto,
      final @NonNull AgentContactInfoDto contactInfoDto,
      final @NonNull StoreDetailDto storeDetailDto
    ) throws NoSuchAgentException, NoSuchStateException, IllegalLGAException {
        final AgentEntity entity = this.agentRepository.findByEmail(email)
                                                                 .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + email + " found"));

        final String agentNameBeforeUpdate = entity.getPersonalInfo().getName();
        if (this.stateLGAManager.validateStateLga(personalInfoDto.state(), personalInfoDto.lga()) || this.stateLGAManager.validateStateLga(storeDetailDto.state(), storeDetailDto.lga())) {
            throw new IllegalLGAException("LGA does not match state");
        }
        this.stateLGAManager.validateStateLga(personalInfoDto.state(),personalInfoDto.lga());
        this.stateLGAManager.validateStateLga(storeDetailDto.state(),storeDetailDto.lga());
        final AgentEntity.PersonalInfoEmbeddable personalInfoEmbeddable = new AgentEntity.PersonalInfoEmbeddable(personalInfoDto.name(), personalInfoDto.state(), personalInfoDto.lga(), personalInfoDto.address());
        final AgentEntity.ContactInfoEmbeddable contactInfoEmbeddable = new AgentEntity.ContactInfoEmbeddable(contactInfoDto.whatsAppPhone(),contactInfoDto.phone());
        final AgentEntity.StoreDetailEmbeddable storeDetailEmbeddable = new AgentEntity.StoreDetailEmbeddable(storeDetailDto.state(),storeDetailDto.lga(), storeDetailDto.address());
        entity.setPersonalInfo(personalInfoEmbeddable);
        entity.setContactInfo(contactInfoEmbeddable);
        entity.setStoreDetail(storeDetailEmbeddable);
        final String agentNameAfterUpdate = entity.getPersonalInfo().getName();
        if (personalInfoDto.name() != null && !agentNameBeforeUpdate.equals(entity.getPersonalInfo().getName())) {
            this.eventPublisher.publishEvent(new AgentNameUpdateEvent(entity.getEmail(),agentNameBeforeUpdate, agentNameAfterUpdate));
        }
        this.agentRepository.save(entity);
        return new AgentInfo(entity);
    }

    public UUID getAgentId(final @NonNull String agentEmail) throws NoSuchAgentException {
        AgentEntity entity = this.agentRepository.findByEmail(agentEmail)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + agentEmail + " found"));
        return entity.getId();
    }

    @Transactional
    LogisticAgentInfo confirmInfoSubmissionForApproval(final @NonNull String email) throws NoSuchAgentException, AgentApprovedException, NoKycRecordFoundException {
        final AgentEntity entity = this.agentRepository.findByEmail(email)
                                                                 .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + email + " found"));
        if (entity.isApproved()) {
            throw new AgentApprovedException("Agent with email " + email + " all ready approved");
        }
        final LogisticKycs kyc = this.kycManager.findAgentKyc(email);
        entity.setKycId(kyc.getKycId());
        this.agentRepository.save(entity);
        final Map<KycDocumentType,String> kycs = this.getKycsPresignedUrl(kyc.getKycs());
        final LogisticAgentInfo agentInfo = new LogisticAgentInfo(entity, kycs);
        this.eventPublisher.publishEvent(new AgentAwaitApproval(agentInfo));
        return agentInfo;
    }


    @Transactional
    LogisticAgent setCurrentlyAcceptingStorage(final @NonNull String email, final boolean status) throws NoSuchAgentException, UnapprovedAgentException {
        final AgentEntity entity = this.agentRepository.findByEmail(email)
                                                       .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + email + " found"));
        if (!entity.isApproved()) {
            throw new UnapprovedAgentException("You have not been approved");
        }
        entity.setAcceptingStorage(status);
        this.agentRepository.save(entity);
        return new LogisticAgent(entity);
    }


    private Map<KycDocumentType,String> getKycsPresignedUrl(final List<LogisticKycDocument> kycDocuments) {
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


}
