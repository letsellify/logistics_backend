package com.letsellify.logistics.components.logistic.core.vendor;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistic.core.financeAccount.event.VendorTopUpAccountEvent;
import com.letsellify.logistics.components.logistic.core.financeAccount.exception.InsufficientFundsException;
import com.letsellify.logistics.components.logistic.core.request.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.PaystackManager;
import com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.rest.resource.PaystackInitiateTransactionResponse;
import com.letsellify.logistics.components.logistic.core.request.LogisticRequestManager;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistic.core.request.exception.InvalidLogisticItemImageException;
import com.letsellify.logistics.components.logistic.core.vendor.data.Vendor;
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
    private final static LogisticAppRole VENDOR_APP_ROLE = LogisticAppRole.VENDOR;


    public PaystackInitiateTransactionResponse initializeTopUp(final @NonNull String vendorEmail, final @NonNull BigDecimal amount) throws UserNotFoundException, VendorNotFoundException {
//        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
//                                                         .orElseThrow(() -> new VendorNotFoundException("Vendor with email " + vendorEmail + " not found."));
        return this.paystackManager.initializePayment(vendorEmail, VENDOR_APP_ROLE, amount);
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
        final String vendorEmail = event.getVendorEmail();
        log.info("Handling VendorTopUpAccountEvent for email: {}", vendorEmail);
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                                                         .orElseThrow(() -> new VendorNotFoundException("Vendor with email " + vendorEmail + " not found"));
        entity.setCurrentAccountBalance(event.getCurrentBalance());
        this.vendorRepository.save(entity);
    }


    public Vendor findVendor(final @NonNull String vendorUsername) throws VendorNotFoundException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorUsername)
                                                         .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorUsername + " not found."));
        return new Vendor(entity);
    }


    public LogisticsItemImage uploadLogisticsItemImage(final @NonNull String vendorEmail, final @NonNull MultipartFile image) throws IOException {
        return this.logisticRequestManager
                 .uploadLogisticsItemImage(vendorEmail,image);
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
    ) throws VendorNotFoundException, InvalidLogisticItemImageException, InsufficientFundsException, NoSuchStateException, IllegalLGAException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                                                         .orElseThrow(() -> new VendorNotFoundException("Vendor with username " + vendorEmail + " not found."));
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



}
