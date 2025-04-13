package com.letsellify.logistics.components.logistic.core.agent;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.communication.core.email.EmailService;
import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.logistic.core.agent.data.AgentPersonalInfo;
import com.letsellify.logistics.components.logistic.core.agent.data.LogisticAgent;
import com.letsellify.logistics.components.logistic.core.agent.data.LogisticAgentInfo;
import com.letsellify.logistics.components.logistic.core.agent.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistic.core.agent.database.repository.AgentRepository;
import com.letsellify.logistics.components.logistic.core.agent.event.AgentAwaitApproval;
import com.letsellify.logistics.components.logistic.core.agent.exception.AgentApprovedException;
import com.letsellify.logistics.components.logistic.core.agent.exception.NoSuchAgentException;
import com.letsellify.logistics.components.logistic.core.agent.exception.UnapprovedAgentException;
import com.letsellify.logistics.components.logistic.core.kyc.KycManager;
import com.letsellify.logistics.components.logistic.core.kyc.data.KycDocumentType;
import com.letsellify.logistics.components.logistic.core.kyc.data.LogisticKycDocument;
import com.letsellify.logistics.components.logistic.core.kyc.data.LogisticKycs;
import com.letsellify.logistics.components.logistic.core.kyc.exception.NoKycRecordFoundException;
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
    private final ApplicationEventPublisher eventPublisher;
    private final FileStorageManager fileStorageManager;
    private final EmailService emailService;


    @Async
    @EventListener
    @Transactional
    public void on(final UserOfRoleAgentCreated event) {
        log.info("Handling AgentCreatedEvent for email: {}", event.getUserEmail());
        // Agent-specific logic here, e.g., assigning delivery zones
        final AgentEntity entity = new AgentEntity(event.getUserEmail(),event.getName());
        this.agentRepository.save(entity);
    }


    @EventListener
    public void on(final LogisticRequestBroadcast broadcast) {
        log.info("we have got a shipping request order from: {}", broadcast.getVendorEmail());
        final List<AgentEntity> agentEntityList = this.agentRepository.findByCurrentlyAcceptingStorageAndApprove(true,true);
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
        if (entity.isApprove()) {
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
        if (entity.isApprove()) {
            throw new AgentApprovedException("Agent with email " + userEmail + " all ready approved");
        }
        return this.kycManager.uploadKyc(entity.getEmail(), LogisticAppRole.DISPATCHER, kycDocumentType, multipartFile);
    }

    @Transactional
    void deleteKyc(final @NonNull String userEmail, final @NonNull String kycId) throws NoSuchAgentException, AgentApprovedException, NoKycRecordFoundException {
        final AgentEntity entity = this.agentRepository.findByEmail(userEmail)
                                                                 .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + userEmail + " found"));
        if (entity.isApprove()) {
            throw new AgentApprovedException("Agent with email " + userEmail + " all ready approved");
        }
        this.kycManager.deleteKyc(userEmail, kycId);

    }

    @Transactional
    AgentPersonalInfo setPersonalInfo(final @NonNull String email, final String whatsAppPhone, final @NonNull String phone, final @NonNull String state, final @NonNull String lga, final @NonNull String address) throws NoSuchAgentException {
        final AgentEntity entity = this.agentRepository.findByEmail(email)
                                                                 .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + email + " found"));
        final AgentEntity.PersonalInfoEmbeddable personalInfoEmbeddable = new AgentEntity.PersonalInfoEmbeddable(whatsAppPhone, phone, state, lga, address);
        entity.setPersonalInfo(personalInfoEmbeddable);
        this.agentRepository.save(entity);
        return new AgentPersonalInfo(personalInfoEmbeddable);
    }

    @Transactional
    LogisticAgentInfo confirmInfoSubmissionForApproval(final @NonNull String email) throws NoSuchAgentException, AgentApprovedException, NoKycRecordFoundException {
        final AgentEntity entity = this.agentRepository.findByEmail(email)
                                                                 .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + email + " found"));
        if (entity.isApprove()) {
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
        if (!entity.isApprove()) {
            throw new UnapprovedAgentException("You have not been approved");
        }
        entity.setCurrentlyAcceptingStorage(status);
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
