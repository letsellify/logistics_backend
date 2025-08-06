package com.letsellify.logistics.components.logistic.core.vendor;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.fileStorage.core.data.StorageType;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.StateLGAManager;
import com.letsellify.logistics.components.logistic.core.request.exception.ImageConflictException;
import com.letsellify.logistics.components.logistic.core.vendor.data.*;
import com.letsellify.logistics.components.logistic.core.vendor.exception.InCompleteVendorProfileException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistic.core.financeAccount.event.VendorTopUpAccountEvent;
import com.letsellify.logistics.components.logistic.core.financeAccount.exception.InsufficientFundsException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.PaystackManager;
import com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.rest.resource.PaystackInitiateTransactionResponse;
import com.letsellify.logistics.components.logistic.core.request.LogisticRequestManager;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistic.core.request.exception.InvalidLogisticItemImageException;
import com.letsellify.logistics.components.logistic.core.vendor.database.entity.VendorEntity;
import com.letsellify.logistics.components.logistic.core.vendor.database.repository.VendorRepository;
import com.letsellify.logistics.components.logistic.core.vendor.exception.VendorExistsException;
import com.letsellify.logistics.components.logistic.core.vendor.exception.VendorNotFoundException;
import com.letsellify.logistics.components.user.core.logisticUser.event.UserOfRoleVendorCreated;
import com.letsellify.logistics.components.user.core.logisticUser.exception.UserNotFoundException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final LogisticRequestManager logisticRequestManager;
    private final FileStorageManager fileStorageManager;
    private final StateLGAManager stateLGAManager;
    private final static LogisticAppRole VENDOR_APP_ROLE = LogisticAppRole.VENDOR;


    public PaystackInitiateTransactionResponse initializeTopUp(final @NonNull String vendorEmail, final @NonNull BigDecimal amount) throws UserNotFoundException, VendorNotFoundException, InCompleteVendorProfileException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                                                         .orElseThrow(() -> new VendorNotFoundException("Vendor with email " + vendorEmail + " not found."));
        this.validateVendor(entity);
        return this.paystackManager.initializePayment(entity.getId(), entity.getEmail(), VENDOR_APP_ROLE , amount);
    }


    @EventListener
    public void handleUserOfRoleVendorCreation(final UserOfRoleVendorCreated event) throws VendorExistsException {
        log.info("Handling VendorCreatedEvent for email: {}", event.getUserEmail() );
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


    public LogisticsItemImage uploadLogisticsItemImage(final @NonNull String vendorEmail, final @NonNull MultipartFile image) throws IOException, InCompleteVendorProfileException, VendorNotFoundException {
        this.fileStorageManager.validateImageFile(image);
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
        this.validateVendor(entity);
        return this.logisticRequestManager
                 .uploadLogisticsItemImage(entity.getId(),entity.getEmail(),image);
    }

    public VendorPersonalInformation uploadPersonalInformation(final @NonNull String vendorEmail, final @NonNull String name, final @NonNull String homeAddress, final @NonNull String state, final @NonNull String lg) throws VendorNotFoundException, NoSuchStateException, IllegalLGAException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
        if (!this.stateLGAManager.validateStateLga(state,lg)) {
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

    public VendorBusinessInformation uploadBusinessInformation(final @NonNull String vendorEmail, final  String businessName, final String businessOfficeAddress, final String state, final String lg) throws VendorNotFoundException, NoSuchStateException, IllegalLGAException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
        if (!this.stateLGAManager.validateStateLga(state,lg)) {
            throw new IllegalLGAException(lg + "does not belong to " + state);
        }
        final VendorEntity.BusinessInformationEmbeddable businessInformationEmbeddable = new VendorEntity.BusinessInformationEmbeddable(businessName, businessOfficeAddress, state, lg);
        entity.setBusinessInformation(businessInformationEmbeddable);
        this.vendorRepository.save(entity);
        return new VendorBusinessInformation(entity.getBusinessInformation());
    }

    public String uploadProfilePicture(final @NonNull String vendorEmail, final @NonNull MultipartFile image) throws IOException, VendorNotFoundException {
        this.fileStorageManager.validateImageFile(image);
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));

        if (entity.getProfilePicture() != null) {
            this.fileStorageManager.deleteFile(entity.getProfilePicture());
        }

        final String fileKey = this.fileStorageManager.storeFile(StorageType.VENDOR_PROFILE_PICTURE, vendorEmail, image);
        entity.setProfilePicture(fileKey);
        this.vendorRepository.save(entity);
        return fileStorageManager.generatePresignedUrl(fileKey);
    }

    public VendorInformation getVendorInformation(final @NonNull String vendorEmail) throws VendorNotFoundException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
        return this.buildVendorInformation(entity);
    }

    public CompletableFuture<String> order(
      final @NonNull String vendorEmail,
      final @NonNull String itemName,
      final int quantity,
      final @NonNull String description,
      final String fragility,
      final @NonNull Set<String> condition,
      final int weight,
      final String imageUrl1,
      final String imageUrl2,
      final @NonNull String receiverFullName,
      final @NonNull String location,
      final @NonNull String state,
      final @NonNull String lga,
      final String email,
      final @NonNull String callPhoneNumber,
      final String whatsAppPhoneNumber,
      final @NonNull BigDecimal agentPay,
      final @NonNull BigDecimal dispatcherPay,
      final @NonNull LocalDate dispatcherPickUpDate,
      final @NonNull LocalDate dispatcherDeliveryDate,
      final @NonNull String pickUpState,
      final @NonNull String pickUpLga,
      final @NonNull String pickUpAddress
    ) throws VendorNotFoundException, InvalidLogisticItemImageException, InsufficientFundsException, NoSuchStateException, IllegalLGAException, InCompleteVendorProfileException, ImageConflictException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                                                         .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
        this.validateVendor(entity);
        return this.logisticRequestManager
                 .order(
                   new Vendor(entity),
                   itemName,
                   quantity,
                   description,
                   fragility,
                   condition,
                   weight,
                   imageUrl1,
                   imageUrl2,
                   receiverFullName,
                   location,
                   state,
                   lga,
                   email,
                   callPhoneNumber,
                   whatsAppPhoneNumber,
                   agentPay,
                   dispatcherPay,
                   dispatcherPickUpDate,
                   dispatcherDeliveryDate,
                   pickUpState,
                   pickUpLga,
                   pickUpAddress
                 );
    }


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
    }

    private VendorInformation buildVendorInformation(VendorEntity entity) {
        return new VendorInformation(
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


}
