package com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.event;


import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.data.PaystackPayment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: Ahmad Buba
 * Date: 8/14/25
 */

@Getter
public class ChargeSuccessEvent extends ApplicationEvent {
    private final PaystackPayment paystackPayment;
    private final UUID userId;
    private final LogisticAppRole userRole;
    private final BigDecimal amount;

    public ChargeSuccessEvent(final PaystackPayment payment) {
        super(payment);
        paystackPayment = payment;
        this.userId = payment.getUserId();
        this.userRole = payment.getUserRole();
        this.amount = payment.getAmount();
    }
}
