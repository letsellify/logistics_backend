package com.letsellify.logistics.components.logistics.core.paystackPaymentCollection;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.dto.ChargeSuccessPayload;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.dto.InitializePaymentDto;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.resource.PaystackPaymentInitializationResource;
import com.letsellify.logistics.components.logistics.core.user.exception.UserNotFoundException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/9/25
 * Time:09:33
 */

@Service
@RequiredArgsConstructor
public class PaystackDataService {
    private final PaystackManager paystackManager;

    public PaystackPaymentInitializationResource initializePayment(final @NonNull Authentication authentication, final @NonNull InitializePaymentDto initializePaymentDto) {
        try {
            return this.paystackManager.initializePayment(authentication.getName(),initializePaymentDto.getAmount()).getResource();
        }
        catch (final UserNotFoundException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public void handleChargeSuccessWebhook(final @NonNull ChargeSuccessPayload payload) {
        this.paystackManager.handleChargeSuccessWebhook(payload);
    }

}
