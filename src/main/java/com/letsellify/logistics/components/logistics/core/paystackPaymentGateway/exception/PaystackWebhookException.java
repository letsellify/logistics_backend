package com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:2/19/25
 * Time:15:30
 */

public class PaystackWebhookException extends LogisticsException {

    public PaystackWebhookException(final String msg) {
        super(msg);
    }

    public PaystackWebhookException(final String msg, final Object... args) {
        super(msg, args);
    }

    public PaystackWebhookException(final Exception cause) {
        super(cause);
    }

}
