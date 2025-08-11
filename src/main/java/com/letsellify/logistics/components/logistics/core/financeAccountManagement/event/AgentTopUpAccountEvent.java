package com.letsellify.logistics.components.logistics.core.financeAccountManagement.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;
import java.util.UUID;

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
