package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.eventStore.command;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDateTime;

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
