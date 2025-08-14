package com.letsellify.logistics.components.logistics.core.vendorManagement;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.fileStorage.core.data.StorageType;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.event.VendorTopUpAccountEvent;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.NigeriaStatesManager;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.PaystackManager;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.resource.PaystackInitiateTransactionResponse;
import com.letsellify.logistics.components.logistics.core.vendorManagement.data.*;
import com.letsellify.logistics.components.logistics.core.vendorManagement.database.entity.VendorEntity;
import com.letsellify.logistics.components.logistics.core.vendorManagement.database.repository.VendorRepository;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.*;
import com.letsellify.logistics.components.user.core.userManagement.event.UserOfRoleVendorCreated;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:55
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class VendorManager {
    private final VendorRepository vendorRepository;
    private final PaystackManager paystackManager;
    private final FileStorageManager fileStorageManager;
    private final NigeriaStatesManager nigeriaStatesManager;
    private final static LogisticAppRole VENDOR_APP_ROLE = LogisticAppRole.VENDOR;


    public PaystackInitiateTransactionResponse initializeTopUp(final @NonNull String vendorEmail, final @NonNull BigDecimal amount) throws UserNotFoundException, VendorNotFoundException, InCompleteVendorProfileException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with email " + vendorEmail + " not found."));
        this.validateVendor(entity);
        return this.paystackManager.initializePayment(entity.getId(), entity.getEmail(), VENDOR_APP_ROLE, amount);
    }


    @EventListener
    public void handleUserOfRoleVendorCreation(final UserOfRoleVendorCreated event) throws VendorExistsException {
        log.info("Handling VendorCreatedEvent for email: {}", event.getUserEmail());
        // Vendor-specific logic here, e.g., notifying the vendor
        final String vendorEmail = event.getUserEmail();
        final String vendorName = event.getName();
        if (this.vendorRepository.existsByEmail(vendorEmail)) {
            throw new VendorExistsException("Vendor with email " + vendorEmail + " all ready exists");
        }
        final VendorEntity entity = VendorEntity.getInstance(vendorEmail, vendorName);
        this.vendorRepository.save(entity);
        log.info("Vendor Created for email: {}", entity.getEmail());
    }

    // this exception is sensitve in this context
    // might mean that vendor changed email or something(inconsistent db homeState)
    // therefore in prod this exception should result in slack or email notification to admin
    @EventListener
    public void handleAccountTopUp(final VendorTopUpAccountEvent event) throws VendorNotFoundException {
        log.info("Handling VendorTopUpAccountEvent for user: {}", event.getVendorId());
        final VendorEntity entity = this.vendorRepository.findById(event.getVendorId())
                .orElseThrow(() -> new VendorNotFoundException("Vendor with id " + event.getVendorId() + " not found"));
        entity.setCurrentAccountBalance(event.getCurrentBalance());
        this.vendorRepository.save(entity);
    }


    public Vendor findVendor(final @NonNull String vendorUsername) throws VendorNotFoundException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorUsername)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorUsername + " not found."));
        return new Vendor(entity);
    }

    public Vendor findVendor(final @NonNull UUID vendorId) throws VendorNotFoundException {
        final VendorEntity entity = this.vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with id " + vendorId + " not found."));
        return new Vendor(entity);
    }



//    public LogisticsItemImage uploadLogisticsItemImage(final @NonNull String vendorEmail, final @NonNull MultipartFile image) throws IOException, InCompleteVendorProfileException, VendorNotFoundException {
//        this.fileStorageManager.validateImageFile(image);
//        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
//                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
//        this.validateVendor(entity);
//        return this.logisticRequestManager
//                .uploadLogisticsItemImage(entity.getId(), entity.getEmail(), image);
//    }

    public VendorInfo setProfile(
            final @NonNull String vendorEmail,
            final String vendorName,
            final @NonNull String homeAddress,
            final @NonNull String homeState,
            final @NonNull String homeLg,
            final @NonNull String phoneNumber,
            final String whatsAppPhoneNumber,
            final String businessName,
            final String businessOfficeAddress,
            final String state,
            final String lg
    ) throws VendorNotFoundException, NoSuchStateException, IllegalLGAException, CompleteVendorProfileException {
        if (!this.nigeriaStatesManager.validateStateLga(homeState, homeLg)) {
            throw new IllegalLGAException(homeLg + "does not belong to " + homeState);
        }
        if (!this.nigeriaStatesManager.validateStateLga(state,lg)) {
            throw new IllegalLGAException(lg + "does not belong to " + state);
        }
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
        if (entity.isProfileComplete()) {
            throw new CompleteVendorProfileException("Vendor profile is all ready filled");
        }
        final VendorEntity.PersonalInformationEmbeddable personalInformationEmbeddable = new VendorEntity.PersonalInformationEmbeddable(vendorName, homeAddress, homeState, homeLg);
        final VendorEntity.ContactInformationEmbeddable contactInformationEmbeddable = new VendorEntity.ContactInformationEmbeddable(phoneNumber, whatsAppPhoneNumber);
        final VendorEntity.BusinessInformationEmbeddable businessInformationEmbeddable = new VendorEntity.BusinessInformationEmbeddable(businessName, businessOfficeAddress, state, lg);
        entity.setPersonalInformation(personalInformationEmbeddable);
        entity.setContactInformation(contactInformationEmbeddable);
        entity.setBusinessInformation(businessInformationEmbeddable);
        String profilePicturePresignedUrl = null;
        if (entity.getProfilePicture() != null) {
            profilePicturePresignedUrl = this.fileStorageManager.generatePresignedUrl(entity.getProfilePicture());
            entity.setProfileComplete(true);
        }
        this.vendorRepository.save(entity);
        return new VendorInfo(profilePicturePresignedUrl, entity);
    }

    public VendorInfo getProfile(final @NonNull String vendorUsername) throws VendorNotFoundException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorUsername)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorUsername + " not found."));
        final String profilePicture = entity.getProfilePicture();
        return new VendorInfo(profilePicture == null ? this.fileStorageManager.generatePresignedUrl(profilePicture): null, entity);
    }

    public VendorPersonalInformation uploadPersonalInformation(final @NonNull String vendorEmail, final @NonNull String name, final @NonNull String homeAddress, final @NonNull String state, final @NonNull String lg) throws VendorNotFoundException, NoSuchStateException, IllegalLGAException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
        if (!this.nigeriaStatesManager.validateStateLga(state, lg)) {
            throw new IllegalLGAException(lg + "does not belong to " + state);
        }
        final VendorEntity.PersonalInformationEmbeddable personalInformationEmbeddable = new VendorEntity.PersonalInformationEmbeddable(name, homeAddress, state, lg);
        entity.setPersonalInformation(personalInformationEmbeddable);
        this.vendorRepository.save(entity);
        return new VendorPersonalInformation(entity.getPersonalInformation());
    }

    public VendorContactInformation uploadContactInformation(final @NonNull String vendorEmail, final @NonNull String phoneNumber, final @NonNull String whatsAppPhoneNumber) throws VendorNotFoundException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
        final VendorEntity.ContactInformationEmbeddable contactInformationEmbeddable = new VendorEntity.ContactInformationEmbeddable(phoneNumber, whatsAppPhoneNumber);
        entity.setContactInformation(contactInformationEmbeddable);
        this.vendorRepository.save(entity);
        return new VendorContactInformation(entity.getContactInformation());
    }

    public VendorBusinessInformation uploadBusinessInformation(final @NonNull String vendorEmail, final String businessName, final String businessOfficeAddress, final String state, final String lg) throws VendorNotFoundException, NoSuchStateException, IllegalLGAException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
        if (!this.nigeriaStatesManager.validateStateLga(state, lg)) {
            throw new IllegalLGAException(lg + "does not belong to " + state);
        }
        final VendorEntity.BusinessInformationEmbeddable businessInformationEmbeddable = new VendorEntity.BusinessInformationEmbeddable(businessName, businessOfficeAddress, state, lg);
        entity.setBusinessInformation(businessInformationEmbeddable);
        this.vendorRepository.save(entity);
        return new VendorBusinessInformation(entity.getBusinessInformation());
    }

    public String uploadProfilePicture(final @NonNull String vendorEmail, final @NonNull MultipartFile image) throws IOException, VendorNotFoundException, VendorProfilePictureExistsException, CompleteVendorProfileException {
        this.fileStorageManager.validateImageFile(image);
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));

        if (entity.isProfileComplete()) {
            throw new CompleteVendorProfileException("Vendor profile has all ready been filled out");
        }

        if (entity.getProfilePicture() != null) {
            throw new VendorProfilePictureExistsException("you cannot update profile picture");
        }

        final String fileKey = this.fileStorageManager.storeFile(StorageType.VENDOR_PROFILE_PICTURE, vendorEmail, image);
        entity.setProfilePicture(fileKey);
        if (entity.getPersonalInformation() != null && entity.getContactInformation() != null && entity.getBusinessInformation() != null) {
            entity.setProfileComplete(true);
        }
        this.vendorRepository.save(entity);
        return fileStorageManager.generatePresignedUrl(fileKey);
    }

//    private void validateVendorProfile(VendorEntity entity) {
//        if (entity.getPersonalInformation() == null) {
//            throw new InCompleteVendorProfileException("Vendor contact ")
//        }
//    }

    public VendorInfo getVendorInformation(final @NonNull String vendorEmail) throws VendorNotFoundException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
        return this.buildVendorInformation(entity);
    }

    public UUID getVendorId(final @NonNull String vendorEmail) throws VendorNotFoundException {
        VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
        return entity.getId();
    }

//    public CompletableFuture<String> order(
//            final @NonNull String vendorEmail,
//            final @NonNull String itemName,
//            final int quantity,
//            final @NonNull String description,
//            final String fragility,
//            final @NonNull Set<String> condition,
//            final int weight,
//            final String imageUrl1,
//            final String imageUrl2,
//            final @NonNull String receiverFullName,
//            final @NonNull String location,
//            final @NonNull String state,
//            final @NonNull String lga,
//            final String email,
//            final @NonNull String callPhoneNumber,
//            final String whatsAppPhoneNumber,
//            final @NonNull BigDecimal agentPay,
//            final @NonNull BigDecimal dispatcherPay,
//            final @NonNull LocalDate dispatcherPickUpDate,
//            final @NonNull LocalDate dispatcherDeliveryDate,
//            final @NonNull String pickUpState,
//            final @NonNull String pickUpLga,
//            final @NonNull String pickUpAddress
//    ) throws VendorNotFoundException, InvalidLogisticItemImageException, InsufficientFundsException, NoSuchStateException, IllegalLGAException, InCompleteVendorProfileException, ImageConflictException {
//        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
//                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
//        this.validateVendor(entity);
//        return this.logisticRequestManager
//                .order(
//                        new Vendor(entity),
//                        itemName,
//                        quantity,
//                        description,
//                        fragility,
//                        condition,
//                        weight,
//                        imageUrl1,
//                        imageUrl2,
//                        receiverFullName,
//                        location,
//                        state,
//                        lga,
//                        email,
//                        callPhoneNumber,
//                        whatsAppPhoneNumber,
//                        agentPay,
//                        dispatcherPay,
//                        dispatcherPickUpDate,
//                        dispatcherDeliveryDate,
//                        pickUpState,
//                        pickUpLga,
//                        pickUpAddress
//                );
//    }


    // here return the order:LogisticsOrder. make the dataservice map back the item

    private void validateVendor(VendorEntity entity) throws InCompleteVendorProfileException {
        if (entity.getProfilePicture() == null) {
            throw new InCompleteVendorProfileException("Profile picture missing");
        }
        if (entity.getContactInformation() == null) {
            throw new InCompleteVendorProfileException("Contact information missing");
        }
        if (entity.getPersonalInformation() == null) {
            throw new InCompleteVendorProfileException("Personal information missing");
        }
        if (entity.getBusinessInformation() == null) {
            throw new InCompleteVendorProfileException("Business information missing");
        }
    }

//    public LogisticRequest getLogisticRequest(final @NonNull String vendorEmail, final @NonNull String logisticRequestId) throws VendorNotFoundException, InCompleteVendorProfileException, NoSuchLogisticRequestException {
//        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
//                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
//        this.validateVendor(entity);
//        return this.logisticRequestManager.getLogisticRequest(entity.getId(), logisticRequestId);
//    }

//    public LogisticRequests getLogisticRequests(final @NonNull String vendorEmail, final @NonNull Pageable pageable) throws VendorNotFoundException, InCompleteVendorProfileException {
//        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
//                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
//        this.validateVendor(entity);
//        return this.logisticRequestManager.getVendorLogisticRequests(entity.getId(), pageable);
//    }

    private VendorInfo buildVendorInformation(VendorEntity entity) {
        return new VendorInfo(
                fileStorageManager.generatePresignedUrl(entity.getProfilePicture()),
                wrapPersonalInfo(entity),
                wrapContactInfo(entity),
                wrapBusinessInfo(entity)
        );
    }

    private VendorPersonalInformation wrapPersonalInfo(VendorEntity e) {
        return e.getPersonalInformation() != null ? new VendorPersonalInformation(e.getPersonalInformation()) : null;
    }

    private VendorContactInformation wrapContactInfo(VendorEntity e) {
        return e.getContactInformation() != null ? new VendorContactInformation(e.getContactInformation()) : null;
    }

    private VendorBusinessInformation wrapBusinessInfo(VendorEntity e) {
        return e.getBusinessInformation() != null ? new VendorBusinessInformation(e.getBusinessInformation()) : null;
    }


    public Vendor findAndValidateVendor(@NonNull String userName) throws VendorNotFoundException, InCompleteVendorProfileException {
        VendorEntity entity = this.vendorRepository.findByEmail(userName)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + userName + " not found."));
        this.validateVendor(entity);
        return new Vendor(entity);
    }

    public Vendor findAndValidateVendor(@NonNull UUID vendorId) throws VendorNotFoundException, InCompleteVendorProfileException {
        VendorEntity entity = this.vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with Id " + vendorId+ " not found."));
        this.validateVendor(entity);
        return new Vendor(entity);
    }

    public void topUpAccount(final @NonNull UUID vendorId, final @NonNull BigDecimal amount) throws VendorNotFoundException, InCompleteVendorProfileException {
        VendorEntity entity = this.vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with Id " + vendorId+ " not found."));
        this.validateVendor(entity);

    }
}
