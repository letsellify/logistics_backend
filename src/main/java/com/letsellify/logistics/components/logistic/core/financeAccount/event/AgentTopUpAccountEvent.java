package com.letsellify.logistics.components.logistic.core.financeAccount.event;

import java.math.BigDecimal;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/22/25
 * Time:11:22
 */

@Getter
public class AgentTopUpAccountEvent extends ApplicationEvent {
    private final String vendorEmail;
    private final BigDecimal currentBalance;

    public AgentTopUpAccountEvent(final String vendorEmail, final BigDecimal currentBalance) {
        super(vendorEmail);
        this.vendorEmail = vendorEmail;
        this.currentBalance = currentBalance;
    }

}
