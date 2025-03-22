package com.letsellify.logistics.components.logistic.core.paystackPaymentGateway;

import org.springframework.stereotype.Service;

import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.exception.PaystackWebhookException;
import com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.rest.dto.ChargeSuccessPayload;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:2/18/25
 * Time:10:52
 */

@Service
@RequiredArgsConstructor
public class PaystackDataService {
    private final PaystackManager paystackManager;

    public void handleChargeSuccessWebhook(final @NonNull ChargeSuccessPayload payload) {
        try {
            this.paystackManager.handleChargeSuccessWebhook(payload);
        }
        catch (PaystackWebhookException e) {
            throw new LogisticsResourceNotFoundException(e);
        }
    }

}
