package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.command;

import java.time.LocalDateTime;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/29/25
 * Time:11:52
 */

@Getter
public class TriggerSettlementCommand {
    @TargetAggregateIdentifier
    private final String requestId;
    private final LocalDateTime triggeredOn;

    public TriggerSettlementCommand(final String requestId) {
        this.requestId = requestId;
        this.triggeredOn = LocalDateTime.now();
    }
}
