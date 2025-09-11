package com.letsellify.logistics.components.logistics.core.agentManagement;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.communication.core.emailManagement.EmailService;
import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.fileStorage.core.data.StorageType;
import com.letsellify.logistics.components.logistics.core.agentManagement.data.*;
import com.letsellify.logistics.components.logistics.core.agentManagement.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistics.core.agentManagement.database.repository.AgentRepository;
import com.letsellify.logistics.components.logistics.core.agentManagement.event.AgentAwaitApproval;
import com.letsellify.logistics.components.logistics.core.agentManagement.event.AgentNameUpdateEvent;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.*;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto.AgentContactInfoDto;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto.AgentPersonalInfoDto;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto.StoreDetailDto;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.Dispatcher;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.DispatcherInfo;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.KycType;
import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorCareer;
import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorRelationship;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        final List<AgentEntity> agentEntityList = this.agentRepository.findByProfileCompleteTrueAndApprovedTrueAndAcceptingStorageTrueAndPersonalInformation_Lga(broadcast.getReceiverLga());
        final Set<Agent> agents = new HashSet<>();
        for (final AgentEntity entity : agentEntityList) {
            agents.add(new Agent(entity));
        }
        // send email
        this.emailService.sendLogisticRequestedMailToAgents(broadcast, agents);
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

//    @Transactional
//    public AgentProfileInfo setInfo(
//            final @NonNull String email,
//            final @NonNull AgentPersonalInfoDto personalInfoDto,
//            final @NonNull AgentContactInfoDto contactInfoDto,
//            final @NonNull StoreDetailDto storeDetailDto
//    ) throws NoSuchAgentException, NoSuchStateException, IllegalLGAException {
//        final AgentEntity entity = this.agentRepository.findByEmail(email)
//                .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + email + " found"));
//
//        final String agentNameBeforeUpdate = entity.getPersonalInformation().getName();
//        if (this.nigeriaStatesManager.validateStateLga(personalInfoDto.state(), personalInfoDto.lga()) || this.nigeriaStatesManager.validateStateLga(storeDetailDto.state(), storeDetailDto.lga())) {
//            throw new IllegalLGAException("LGA does not match state");
//        }
//        final AgentEntity.PersonalInfoEmbeddable personalInfoEmbeddable = new AgentEntity.PersonalInfoEmbeddable(personalInfoDto.name(), personalInfoDto.state(), personalInfoDto.lga(), personalInfoDto.address());
//        final AgentEntity.ContactInfoEmbeddable contactInfoEmbeddable = new AgentEntity.ContactInfoEmbeddable(contactInfoDto.whatsAppPhone(), contactInfoDto.phone());
//        final AgentEntity.StoreDetailEmbeddable storeDetailEmbeddable = new AgentEntity.StoreDetailEmbeddable()
//        entity.setPersonalInformation(personalInfoEmbeddable);
//        entity.setContactInformation(contactInfoEmbeddable);
//        entity.setBusinessInformation(storeDetailEmbeddable);
//        final String agentNameAfterUpdate = entity.getPersonalInformation().getName();
//        if (personalInfoDto.name() != null && !agentNameBeforeUpdate.equals(entity.getPersonalInformation().getName())) {
//            this.eventPublisher.publishEvent(new AgentNameUpdateEvent(entity.getEmail(), agentNameBeforeUpdate, agentNameAfterUpdate));
//        }
//        this.agentRepository.save(entity);
//        return new AgentProfileInfo(entity);
//    }

    public UUID getAgentId(final @NonNull String agentEmail) throws NoSuchAgentException {
        AgentEntity entity = this.agentRepository.findByEmail(agentEmail)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with email " + agentEmail + " found"));
        return entity.getId();
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

    @Transactional
    AgentInfo setProfile(
            final @NonNull String agentUsername,
            final @NonNull String agentFullName,
            final @NonNull String agentState,
            final @NonNull String agentLga,
            final @NonNull String agentAddress,
            final @NonNull String agentPhone,
            final String agentWhatsAppPhone,
            final String businessName,
            final @NonNull String businessState,
            final @NonNull String businessLga,
            final @NonNull String businessAddress,
            final @NonNull String guarantorFullName,
            final @NonNull String guarantorEmail,
            final @NonNull String guarantorState,
            final @NonNull String guarantorLga,
            final @NonNull String guarantorAddress,
            final @NonNull String guarantorPhone,
            final @NonNull String guarantorWhatsAppPhone,
            final @NonNull GuarantorCareer guarantorCareer,
            final @NonNull GuarantorRelationship guarantorRelationship,
            final @NonNull KycType kycType,
            final @NonNull String kycNumber
    ) throws IllegalLGAException, NoSuchStateException, NoSuchAgentException, AgentProfileCompleteException {
        if (!this.nigeriaStatesManager.validateStateLga(agentState,agentLga)) {
            throw new IllegalLGAException("LGA " + agentLga + " does not belong to state " + agentState);
        }
        if (!this.nigeriaStatesManager.validateStateLga(guarantorState,guarantorLga)) {
            throw new IllegalLGAException("LGA " + guarantorLga + " does not belong to state " + guarantorState);
        }

        if (!this.nigeriaStatesManager.validateStateLga(businessState,businessLga)) {
            throw new IllegalLGAException("LGA " + businessLga + " does not belong to state " + businessState);
        }

        AgentEntity entity = this.agentRepository.findByEmail(agentUsername)
                .orElseThrow(() -> new NoSuchAgentException("No such agent with username " + agentUsername + " found"));
        if (entity.isProfileComplete()) {
            throw new AgentProfileCompleteException("Agent profile has all ready been filled");
        }
        if (entity.getPersonalInformation() == null) {
            final AgentEntity.PersonalInfoEmbeddable personalInformation = AgentEntity.PersonalInfoEmbeddable.builder()
                    .name(agentFullName)
                    .state(agentState)
                    .lga(agentLga)
                    .address(agentAddress)
                    .build();
            entity.setPersonalInformation(personalInformation);
        }
        else {
            final String agentNameBeforeUpdate = entity.getPersonalInformation().getName();
            final AgentEntity.PersonalInfoEmbeddable personalInformation = AgentEntity.PersonalInfoEmbeddable.builder()
                    .name(agentFullName)
                    .state(agentState)
                    .lga(agentLga)
                    .address(agentAddress)
                    .build();
            entity.setPersonalInformation(personalInformation);
            final String agentNameAfterUpdate = entity.getPersonalInformation().getName();
            if (!agentNameBeforeUpdate.equals(agentNameAfterUpdate)) {
                this.eventPublisher.publishEvent(new AgentNameUpdateEvent(entity.getEmail(), agentNameBeforeUpdate, agentNameAfterUpdate));
            }
        }
        final AgentEntity.ContactInfoEmbeddable contactInfoEmbeddable = AgentEntity.ContactInfoEmbeddable.builder()
                .phone(agentPhone)
                .whatsAppPhone(agentWhatsAppPhone)
                .build();
        final AgentEntity.StoreDetailEmbeddable storeDetailEmbeddable = AgentEntity.StoreDetailEmbeddable.builder()
                .businessName(businessName)
                .businessState(businessState)
                .businessLga(businessLga)
                .businessOfficeAddress(businessAddress)
                .build();

        final AgentEntity.AgentGuarantorEmbeddable guarantorEmbeddable = AgentEntity.AgentGuarantorEmbeddable.builder()
                .fullName(guarantorFullName)
                .email(guarantorEmail)
                .state(guarantorState)
                .lga(guarantorLga)
                .address(guarantorAddress)
                .phoneNumber(guarantorPhone)
                .whatsAppNumber(guarantorWhatsAppPhone)
                .relationship(guarantorRelationship)
                .career(guarantorCareer)
                .build();
        entity.setContactInformation(contactInfoEmbeddable);
        entity.setGuarantor(guarantorEmbeddable);
        entity.setBusinessInformation(storeDetailEmbeddable);
        entity.setKycType(kycType);
        entity.setKycNumber(kycNumber);
        String profilePicturePresignedUrl = null;
        if (entity.getProfileImage() != null) {
            profilePicturePresignedUrl = this.fileStorageManager.generatePresignedUrl(entity.getProfileImage());
            entity.setProfileComplete(true);
        }
        this.agentRepository.save(entity);
        return new AgentInfo(profilePicturePresignedUrl, entity);
    }

    AgentInfo getProfile(final @NonNull String agentUsername) throws NoSuchAgentException {
        final AgentEntity entity = this.agentRepository.findByEmail(agentUsername)
                .orElseThrow(() -> new NoSuchAgentException("Agent with username " + agentUsername + " does not exist"));
        final String profilePicturePresignedUrl = entity.getProfileImage() == null ? null : this.fileStorageManager.generatePresignedUrl(entity.getProfileImage());
        return new AgentInfo(profilePicturePresignedUrl, entity);
    }

    @Transactional
    String uploadProfilePhoto(final @NonNull String agentUsername, final @NonNull MultipartFile file) throws NoSuchAgentException, AgentProfileCompleteException, AgentProfilePhotoExistsException, IOException {
        this.fileStorageManager.validateImageFile(file);
        final AgentEntity entity = this.agentRepository.findByEmail(agentUsername)
                .orElseThrow(() -> new NoSuchAgentException("Agent with username " + agentUsername + " does not exist"));
        if (entity.isProfileComplete()) {
            throw new AgentProfileCompleteException("You have all ready filled your profile, You cannot edit anything. Contact admin");
        }
        if (entity.getProfileImage() != null) {
            throw new AgentProfilePhotoExistsException("you cannot update profile picture");
        }
        final String fileKey = this.fileStorageManager.storeFile(StorageType.AGENT_PROFILE_PICTURE, agentUsername, file);
        entity.setProfileImage(fileKey);
        if (entity.getPersonalInformation() != null && entity.getContactInformation() != null && entity.getGuarantor() != null && entity.getBusinessInformation()!= null && entity.getKycType() != null && entity.getKycNumber() != null) {
            entity.setProfileComplete(true);
        }
        this.agentRepository.save(entity);
        return this.fileStorageManager.generatePresignedUrl(fileKey);
    }

    @Transactional
    public Agent approveAgent(final @NonNull String agentUsername) throws NoSuchAgentException, InCompleteAgentProfileException, AgentApprovedException {
        final AgentEntity entity = this.agentRepository.findByEmail(agentUsername)
                .orElseThrow(() -> new NoSuchAgentException("Agent with username " + agentUsername + " does not exist"));
        if (!entity.isProfileComplete()) {
            throw new InCompleteAgentProfileException("Agent profile not complete");
        }
        if (entity.isApproved()) {
            throw new AgentApprovedException("Agent with username " + agentUsername + " all ready approved");
        }
        entity.setApproved(true);
        this.agentRepository.save(entity);
        /* possibly send email to agent using email service */
        return new Agent(entity);
    }

    public AgentsInfo getAllAwaitingApproval(final @NonNull Pageable pageable) {
        final Page<AgentEntity> entityPage = this.agentRepository.findByApprovedFalseAndProfileCompleteTrue(pageable);
        log.info(entityPage.getContent().toString());
        final List<AgentEntity> entities = entityPage.getContent();
        final List<AgentInfo> agentInfos = new ArrayList<>();
        for (final AgentEntity entity : entities) {
            final String profilePicturePresignedUrl = entity.getProfileImage() == null ? null : this.fileStorageManager.generatePresignedUrl(entity.getProfileImage());
            agentInfos.add(new AgentInfo(profilePicturePresignedUrl, entity));
        }
        return new AgentsInfo(agentInfos, entityPage);
    }
}
