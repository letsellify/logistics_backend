package com.letsellify.logistics.components.logistic.core.financeAccount.event;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/22/25
 * Time:11:22
 */

@Getter
public class DispatcherTopUpAccountEvent extends ApplicationEvent {
    private final UUID dispatcherId;
    private final BigDecimal currentBalance;

    public DispatcherTopUpAccountEvent(final UUID dispatcherId, final BigDecimal currentBalance) {
        super(dispatcherId);
        this.dispatcherId = dispatcherId;
        this.currentBalance = currentBalance;
    }

}
