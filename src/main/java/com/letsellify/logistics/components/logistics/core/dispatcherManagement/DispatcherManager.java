package com.letsellify.logistics.components.logistics.core.dispatcherManagement;

import com.letsellify.logistics.components.communication.core.emailManagement.EmailService;
import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.fileStorage.core.data.StorageType;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.Dispatcher;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.DispatcherInfo;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.DispatchersInfo;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.repository.DispatcherRepository;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.event.DispatcherNameUpdateEvent;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.*;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.*;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.event.LogisticRequestBroadcast;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.NigeriaStatesManager;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.DispatcherProfilePhotoExistsException;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.VendorExistsException;
import com.letsellify.logistics.components.user.core.userManagement.event.UserOfRoleDispatcherCreated;
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

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:03
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class DispatcherManager {
    private final DispatcherRepository dispatcherRepository;
    private final NigeriaStatesManager nigeriaStatesManager;
    private final ApplicationEventPublisher eventPublisher;
    private final FileStorageManager fileStorageManager;
    private final EmailService emailService;

    @Async
    @EventListener
    @Transactional
    public void on(final @NonNull UserOfRoleDispatcherCreated event) throws VendorExistsException, DispatcherExistsException {
        log.info("Handling DispatcherCreatedEvent for email: {}", event.getUserEmail());
        // Dispatcher-specific logic here, e.g., assigning dispatch regions
        final String dispatcherEmail = event.getUserEmail();
        final String dispatcherName = event.getName();
        if (this.dispatcherRepository.existsByEmail(dispatcherEmail)) {
            throw new DispatcherExistsException("Dispatcher with email " + dispatcherEmail + " all ready exists");
        }
        final DispatcherEntity entity = DispatcherEntity.getInstance(dispatcherEmail, dispatcherName);
        this.dispatcherRepository.save(entity);
    }

    // listen for this event in agent and dispatcher module
    // for dispatcher get all open availabilties that fall within the date, homeState and homeLga
    // returns a list. filter to make sure you only send to the dispatcher 1s
    // 1 email could be for 2 possible slots
    // a core logic
    // check vendors accepting at the moment,
    // check if the start and end date is same
    // if same, just check vendors available from that day up wards
    // else check
    // send them an email which forces them online
    // this is because the email could contain just basic things(location, no images)
    // but then when the try to access it online
    // it interacts directly with shippingRequestDataService
    @EventListener
    public void on(final LogisticRequestBroadcast broadcast) {
        log.info("we have got a shipping request order from: {}", broadcast.getSenderId());
        final List<DispatcherEntity> dispatcherEntityList = this.dispatcherRepository.findByCurrentlyAcceptingDeliveryAndApprove(true, true);
        final Set<Dispatcher> dispatchers = new HashSet<>();
        for (final DispatcherEntity entity : dispatcherEntityList) {
            dispatchers.add(new Dispatcher(entity));
        }
        // send email
        this.emailService.sendLogisticRequestedMailToDispatchers(broadcast, dispatchers);
    }


    //internal method
    public Dispatcher findDispatcher(final @NonNull String dispatcherUsername) throws NoSuchDispatcherException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(dispatcherUsername)
                .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + dispatcherUsername + " found"));
        return new Dispatcher(entity);
    }

    //internal method
    public Dispatcher findDispatcher(final @NonNull UUID dispatcherId) throws NoSuchDispatcherException {
        final DispatcherEntity entity = this.dispatcherRepository.findById(dispatcherId)
                .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with id " + dispatcherId + " found"));
        return new Dispatcher(entity);
    }


    public DispatchersInfo getAllAwaitingApproval(Pageable pageable) {
        Page<DispatcherEntity> entityPage = this.dispatcherRepository.findByProfileCompleteTrueAndApproveFalse(pageable);
        List<DispatcherEntity> entities = entityPage.getContent();
        List<DispatcherInfo> dispatcherInfos = new ArrayList<>();
        for (DispatcherEntity entity : entities) {
            String profilePicturePresignedUrl = entity.getProfileImage() == null ? null : this.fileStorageManager.generatePresignedUrl(entity.getProfileImage());
            dispatcherInfos.add(new DispatcherInfo(profilePicturePresignedUrl, entity));
        }
        return new DispatchersInfo(dispatcherInfos, entityPage);
    }

    @Transactional
    public Dispatcher approveDispatcher(final @NonNull String dispatcherUsername) throws NoSuchDispatcherException, InCompleteDispatcherProfileException, DispatcherApprovedException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(dispatcherUsername)
                .orElseThrow(() -> new NoSuchDispatcherException("Dispatcher with username " + dispatcherUsername + " does not exist"));
        if (!entity.isProfileComplete()) {
            throw new InCompleteDispatcherProfileException("Dispatcher profile not complete");
        }
        if (entity.isApprove()) {
            throw new DispatcherApprovedException("Dispatcher with username " + dispatcherUsername + " all ready approved");
        }
        entity.setApprove(true);
        this.dispatcherRepository.save(entity);
        /* possibly send email to dispatcher using email service */
        return new Dispatcher(entity);
    }

    public UUID getDispatcherId(final @NonNull String dispatcherUsername) throws NoSuchDispatcherException {
        DispatcherEntity entity = this.dispatcherRepository.findByEmail(dispatcherUsername)
                .orElseThrow(() -> new NoSuchDispatcherException("No dispatcher with email " + dispatcherUsername + " found"));
        return entity.getId();
    }


    @Transactional
    String uploadProfilePhoto(final @NonNull String dispatcherUsername, final @NonNull MultipartFile file) throws NoSuchDispatcherException, DispatcherProfileCompleteException, DispatcherProfilePhotoExistsException, IOException {
        this.fileStorageManager.validateImageFile(file);
        DispatcherEntity entity = this.dispatcherRepository.findByEmail(dispatcherUsername)
                .orElseThrow(() -> new NoSuchDispatcherException("No dispatcher with email " + dispatcherUsername + " found"));
        if (entity.isProfileComplete()) {
            throw new DispatcherProfileCompleteException("You have all ready filled your profile, You cannot edit anything. Contact admin");
        }
        if (entity.getProfileImage() != null) {
            throw new DispatcherProfilePhotoExistsException("you cannot update profile picture");
        }

        final String fileKey = this.fileStorageManager.storeFile(StorageType.DISPATCHER_PROFILE_PICTURE, dispatcherUsername, file);
        entity.setProfileImage(fileKey);
        if (entity.getPersonalInformation() != null && entity.getContactInformation() != null && entity.getGuarantor() != null && entity.getDispatchDetail() != null && entity.getKycType() != null && entity.getKycNumber() != null) {
            entity.setProfileComplete(true);
        }
        this.dispatcherRepository.save(entity);
        return this.fileStorageManager.generatePresignedUrl(fileKey);
    }

    @Transactional
    DispatcherInfo setProfile(
            final @NonNull String dispatcherUsername,
            final @NonNull DispatcherPersonalInfoDto personalInfoDto,
            final @NonNull DispatcherContactInfoDto contactInfoDto,
            final @NonNull DispatchDetailDto dispatchDetailDto,
            final @NonNull DispatcherGuarantorDto dispatcherGuarantorDto,
            final @NonNull KycDto kycDto
    ) throws NoSuchDispatcherException, NoSuchStateException, IllegalLGAException, DispatcherProfileCompleteException {
        if (!this.nigeriaStatesManager.validateStateLga(personalInfoDto.state(), personalInfoDto.lga()) || !this.nigeriaStatesManager.validateStateLga(dispatchDetailDto.state(), dispatchDetailDto.lga())) {
            throw new IllegalLGAException("LGA state validation failed");
        }
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(dispatcherUsername)
                .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + dispatcherUsername + " found"));
        if (entity.isProfileComplete()) {
            throw new DispatcherProfileCompleteException("Dispatcher profile has all ready been filled");
        }
        if (entity.getPersonalInformation() == null) {
            DispatcherEntity.PersonalInfoEmbeddable personalInfoEmbeddable = new DispatcherEntity.PersonalInfoEmbeddable();
            personalInfoEmbeddable.setName(personalInfoDto.name());
            personalInfoEmbeddable.setState(personalInfoDto.state());
            personalInfoEmbeddable.setAddress(personalInfoDto.address());
            personalInfoEmbeddable.setLga(personalInfoDto.lga());
            entity.setPersonalInformation(personalInfoEmbeddable);
        } else {
            final String dispatcherNameBeforeUpdate = entity.getPersonalInformation().getName();

            final DispatcherEntity.PersonalInfoEmbeddable personalInfoEmbeddable = DispatcherEntity.PersonalInfoEmbeddable.builder()
                    .name(personalInfoDto.name())
                    .state(personalInfoDto.state())
                    .lga(personalInfoDto.lga())
                    .address(personalInfoDto.address())
                    .build();
            entity.setPersonalInformation(personalInfoEmbeddable);
            final String dispatcherNameAfterUpdate = entity.getPersonalInformation().getName();
            if (personalInfoDto.name() != null && !dispatcherNameBeforeUpdate.equals(dispatcherNameAfterUpdate)) {
                this.eventPublisher.publishEvent((new DispatcherNameUpdateEvent(entity.getEmail(), dispatcherNameBeforeUpdate, dispatcherNameAfterUpdate)));
            }
        }

        final DispatcherEntity.ContactInfoEmbeddable contactInfoEmbeddable = DispatcherEntity.ContactInfoEmbeddable.builder()
                .phone(contactInfoDto.phone())
                .whatsAppPhone(contactInfoDto.whatsAppPhone())
                .build();
        final DispatcherEntity.DispatchDetailEmbeddable dispatchDetailEmbeddable = DispatcherEntity.DispatchDetailEmbeddable.builder()
                .businessLga(dispatchDetailDto.lga())
                .businessName(dispatchDetailDto.name())
                .businessOfficeAddress(dispatchDetailDto.address())
                .businessState(dispatchDetailDto.state())
                .build();
        final DispatcherEntity.DispatcherGuarantorEmbeddable guarantorEmbeddable = DispatcherEntity.DispatcherGuarantorEmbeddable.builder()
                .fullName(dispatcherGuarantorDto.fullName())
                .address(dispatcherGuarantorDto.address())
                .career(dispatcherGuarantorDto.career())
                .phoneNumber(dispatcherGuarantorDto.phoneNumber())
                .whatsAppNumber(dispatcherGuarantorDto.whatsAppNumber())
                .relationship(dispatcherGuarantorDto.relationship())
                .state(dispatcherGuarantorDto.state())
                .lga(dispatcherGuarantorDto.lga())
                .email(dispatcherGuarantorDto.email())
                .build();
        entity.setContactInformation(contactInfoEmbeddable);
        entity.setDispatchDetail(dispatchDetailEmbeddable);
        entity.setGuarantor(guarantorEmbeddable);
        entity.setKycType(kycDto.type());
        entity.setKycNumber(kycDto.number());
        String profilePicturePresignedUrl = null;
        if (entity.getProfileImage() != null) {
            profilePicturePresignedUrl = this.fileStorageManager.generatePresignedUrl(entity.getProfileImage());
            entity.setProfileComplete(true);
        }
        this.dispatcherRepository.save(entity);
        return new DispatcherInfo(profilePicturePresignedUrl, entity);
    }


    DispatcherInfo getProfile(final @NonNull String dispatcherUsername) throws NoSuchDispatcherException, InCompleteDispatcherProfileException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(dispatcherUsername)
                .orElseThrow(() -> new NoSuchDispatcherException("Dispatcher with username " + dispatcherUsername + " does not exist"));
        if (!entity.isProfileComplete()) {
            throw new InCompleteDispatcherProfileException("Dispatcher profile not complete");
        }
        final String profilePicturePresignedUrl = entity.getProfileImage() == null ? null : this.fileStorageManager.generatePresignedUrl(entity.getProfileImage());
        return new DispatcherInfo(profilePicturePresignedUrl, entity);
    }


    private void validateDispatcher(final @NonNull DispatcherEntity entity) throws InCompleteDispatcherProfileException {
        if (!entity.isProfileComplete()) {
            throw new InCompleteDispatcherProfileException("Dispatcher profile not complete");
        }

        if (entity.getDispatchDetail() == null) {
            throw new InCompleteDispatcherProfileException("Dispatcher business details missing");
        }
        if (entity.getProfileImage() == null) {
            throw new InCompleteDispatcherProfileException("Dispatcher profile image missing");
        }
        if (entity.getPersonalInformation() == null) {
            throw new InCompleteDispatcherProfileException("Dispatcher personal info missing");
        }
        if (entity.getContactInformation() == null) {
            throw new InCompleteDispatcherProfileException("Dispatcher contact info missing");
        }
        if (entity.getGuarantor() == null) {
            throw new InCompleteDispatcherProfileException("Dispatcher Guarantor info missing");
        }
        if (entity.getKycType() == null || entity.getKycNumber() == null) {
            throw new InCompleteDispatcherProfileException("Kyc details missing");
        }
        if (!entity.isApprove()) {
            throw new InCompleteDispatcherProfileException("Dispatcher profile not approved");
        }
    }


}
