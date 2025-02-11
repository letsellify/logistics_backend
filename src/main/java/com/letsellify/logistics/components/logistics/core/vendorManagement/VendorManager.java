package com.letsellify.logistics.components.logistics.core.vendorManagement;

import java.math.BigDecimal;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.letsellify.logistics.common.data.LogisticsAppRole;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.event.VendorTopUpAccountEvent;
import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.StateLGAManager;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.PaystackManager;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.data.Payment;
import com.letsellify.logistics.components.logistics.core.vendorManagement.data.Vendor;
import com.letsellify.logistics.components.logistics.core.vendorManagement.database.entity.VendorEntity;
import com.letsellify.logistics.components.logistics.core.vendorManagement.database.repository.VendorRepository;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.VendorExistsException;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.VendorNotFoundException;
import com.letsellify.logistics.components.user.core.userManagement.event.UserOfRoleVendorCreated;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;

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
    private final CommandGateway commandGateway;
    private final PaystackManager paystackManager;
    private final StateLGAManager stateLGAManager;
    private final static LogisticsAppRole VENDOR_APP_ROLE = LogisticsAppRole.VENDOR;


    public Payment initializePaystackPayment(final @NonNull String vendorEmail, final @NonNull BigDecimal amount) throws UserNotFoundException, VendorNotFoundException {
        final VendorEntity entity = this.vendorRepository.findByEmail(vendorEmail)
                                                         .orElseThrow(() -> new VendorNotFoundException("Vendor with email " + vendorEmail + " not found."));
        return this.paystackManager.initializePayment(vendorEmail, VENDOR_APP_ROLE, amount);
    }


    @EventListener
    public void handleUserOfRoleVendorCreation(final UserOfRoleVendorCreated event) throws VendorExistsException {
        final String vendorEmail = event.getUserEmail();
        log.info("Handling VendorCreatedEvent for email: {}", vendorEmail);
        // Vendor-specific logic here, e.g., notifying the vendor
        if (this.vendorRepository.existsByEmail(vendorEmail)) {
            throw new VendorExistsException("Vendor with email " + vendorEmail + " all ready exists");
        }
        final VendorEntity entity = VendorEntity.getInstance(vendorEmail);
        this.vendorRepository.save(entity);
        log.info("Vendor Created for email: {}", entity.getEmail());
    }

    // this exception is sensitve in this context
    // might mean that vendor changed email or something(inconsistent db state)
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




    // here return the order:LogisticsOrder. make the dataservice map back the item



}
