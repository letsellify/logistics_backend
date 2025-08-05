package com.letsellify.logistics.components.logistic.core.vendor;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.common.restException.LogisticsInternalServerErrorException;
import com.letsellify.logistics.common.restException.LogisticsRestException;
import com.letsellify.logistics.components.logistic.core.financeAccount.exception.InsufficientFundsException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.rest.resource.PaystackInitiateTransactionResponse;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistic.core.request.exception.InvalidLogisticItemImageException;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.LogisticItemImageResource;
import com.letsellify.logistics.components.logistic.core.vendor.exception.InCompleteVendorProfileException;
import com.letsellify.logistics.components.logistic.core.vendor.exception.VendorNotFoundException;
import com.letsellify.logistics.components.logistic.core.vendor.rest.dto.*;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorBusinessInformationResource;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorContactInformationResource;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorInformationResource;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorPersonalInformationResource;
import com.letsellify.logistics.components.user.core.logisticUser.exception.UserNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:56
 */

@Service
@RequiredArgsConstructor
public class VendorDataService {
    private final VendorManager vendorManager;

    public VendorPersonalInformationResource uploadPersonalInformation(final Authentication authentication, final PersonalInformationDto personalInformationDto) {
        try {
            return this.vendorManager.uploadPersonalInformation(
                            authentication.getName(),
                            personalInformationDto.name(),
                            personalInformationDto.homeAddress(),
                            personalInformationDto.state(),
                            personalInformationDto.lg()
                    )
                    .getResource();
        } catch (VendorNotFoundException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (NoSuchStateException | IllegalLGAException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public VendorContactInformationResource uploadContactInformation(final Authentication authentication, final ContactInformationDto contactInformationDto) {
        try {
            return this.vendorManager.uploadContactInformation(
                    authentication.getName(),
                    contactInformationDto.phoneNumber(),
                    contactInformationDto.whatsAppPhoneNumber()
            ).getResource();
        } catch (VendorNotFoundException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public VendorBusinessInformationResource uploadBusinessInformation(final Authentication authentication, final BusinessInformationDto businessInformationDto) {
        try {
            return this.vendorManager.uploadBusinessInformation(
                            authentication.getName(),
                            businessInformationDto.businessName(),
                            businessInformationDto.businessOfficeAddress(),
                            businessInformationDto.state(),
                            businessInformationDto.lg()
                    )
                    .getResource();
        } catch (VendorNotFoundException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (NoSuchStateException | IllegalLGAException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public String uploadProfilePicture(final Authentication authentication, final MultipartFile file) {
        try {
            return this.vendorManager.uploadProfilePicture(authentication.getName(), file);
        } catch (VendorNotFoundException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IOException e) {
            throw new LogisticsInternalServerErrorException(e.getMessage());
        }

    }

    public VendorInformationResource getVendorInformation(final Authentication authentication) {
        try {
            return this.vendorManager.getVendorInformation(authentication.getName()).getResource();
        } catch (VendorNotFoundException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    public PaystackInitiateTransactionResponse initializeTopUp(final Authentication authentication, final VendorTopUpDto topUp) {
        System.out.println("Amount received " + topUp.amount());
        try {
            return this.vendorManager.initializeTopUp(authentication.getName(), topUp.amount());
        } catch (final UserNotFoundException | VendorNotFoundException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        } catch (final InCompleteVendorProfileException e) {
            throw new LogisticsRestException(HttpStatus.FORBIDDEN, "403", e.getMessage());
        }
    }

    public CompletableFuture<String> order(final Authentication authentication, final @NonNull OrderDto orderDto) {
        try {
            return this.vendorManager.order(
                    authentication.getName(),
                    orderDto.goodInformation().itemName(),
                    orderDto.goodInformation().quantity(),
                    orderDto.goodInformation().description(),
                    orderDto.goodInformation().fragility(),
                    orderDto.goodInformation().condition(),
                    orderDto.goodInformation().weight(),
                    orderDto.goodInformation().imageUrl1(),
                    orderDto.goodInformation().imageUrl2(),
                    orderDto.receiverInformation().fullName(),
                    orderDto.receiverInformation().location(),
                    orderDto.receiverInformation().state(),
                    orderDto.receiverInformation().lga(),
                    orderDto.receiverInformation().email(),
                    orderDto.receiverInformation().callPhoneNumber(),
                    orderDto.receiverInformation().whatsAppPhoneNumber(),
                    orderDto.otherDetails()
                            .agentPay(),
                    orderDto.otherDetails()
                            .dispatcherPay(),
                    orderDto.otherDetails()
                            .dispatcherPickUpDate(),
                    orderDto.otherDetails()
                            .dispatcherDeliveryDate(),
                    orderDto.otherDetails().pickUpState(),
                    orderDto.otherDetails().pickUpLga(),
                    orderDto.otherDetails().pickUpAddress()
            );
        } catch (final VendorNotFoundException | InvalidLogisticItemImageException | InsufficientFundsException |
                       NoSuchStateException | IllegalLGAException | InCompleteVendorProfileException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public LogisticItemImageResource uploadLogisticsItemImage(final @NonNull Authentication authentication, final @NonNull MultipartFile file) {
        try {
            return this.vendorManager.uploadLogisticsItemImage(authentication.getName(), file)
                    .getResource();
        } catch (final IOException e) {
            throw new LogisticsInternalServerErrorException(e.getMessage());
        } catch (InCompleteVendorProfileException | VendorNotFoundException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
