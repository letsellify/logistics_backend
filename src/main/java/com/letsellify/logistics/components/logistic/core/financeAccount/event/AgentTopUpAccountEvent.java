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
public class AgentTopUpAccountEvent extends ApplicationEvent {
    private final UUID agentId;
    private final BigDecimal currentBalance;

    public AgentTopUpAccountEvent(final UUID agentId, final BigDecimal currentBalance) {
        super(agentId);
        this.agentId = agentId;
        this.currentBalance = currentBalance;
    }

}
