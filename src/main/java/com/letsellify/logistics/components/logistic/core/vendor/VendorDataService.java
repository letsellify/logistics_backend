package com.letsellify.logistics.components.logistic.core.vendor;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.common.restException.LogisticsInternalServerErrorException;
import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.logistic.core.financeAccount.exception.InsufficientFundsException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.rest.resource.PaystackInitiateTransactionResponse;
import com.letsellify.logistics.components.logistic.core.request.exception.InvalidLogisticItemImageException;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.LogisticItemImageResource;
import com.letsellify.logistics.components.logistic.core.vendor.exception.VendorNotFoundException;
import com.letsellify.logistics.components.logistic.core.vendor.rest.dto.OrderDto;
import com.letsellify.logistics.components.logistic.core.vendor.rest.dto.VendorTopUpDto;
import com.letsellify.logistics.components.user.core.logisticUser.exception.UserNotFoundException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:56
 */

@Service
@RequiredArgsConstructor
public class VendorDataService {
    private final VendorManager vendorManager;
    private final FileStorageManager fileStorageManager;

    public PaystackInitiateTransactionResponse initializeTopUp(final Authentication authentication, final VendorTopUpDto topUp) {
        System.out.println("Amount recieved " + topUp.amount());
        try {
            return this.vendorManager.initializeTopUp(authentication.getName(), topUp.amount());
        }
        catch (final UserNotFoundException | VendorNotFoundException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public CompletableFuture<String> order(final Authentication authentication, final @NonNull OrderDto orderDto) {
        try {
            return this.vendorManager.order(
                         authentication.getName(),
                         orderDto.itemName(),
                         orderDto.description(),
                         orderDto.amountForShipping(),
                         orderDto.amountForStorage(),
                         orderDto.images(),
                         orderDto.currentState(),
                         orderDto.currentLga(),
                         orderDto.shippingState(),
                         orderDto.shippingLga(),
                         orderDto.possibleDeliveryDateStart(),
                         orderDto.possibleDeliveryDateEnd()
                       );
        }

        catch (final VendorNotFoundException | InvalidLogisticItemImageException | InsufficientFundsException | NoSuchStateException | IllegalLGAException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public LogisticItemImageResource uploadLogisticsItemImage(final @NonNull Authentication authentication, final @NonNull MultipartFile file) {
        try {
            this.fileStorageManager.validateImageFile(file);
            return this.vendorManager.uploadLogisticsItemImage(authentication.getName(),file)
                                              .getResource();
        }
        catch (final IOException e) {
            throw new LogisticsInternalServerErrorException(e.getMessage());
        }
    }
}
