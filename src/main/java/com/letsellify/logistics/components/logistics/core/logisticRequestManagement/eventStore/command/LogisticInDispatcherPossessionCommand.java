package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.eventStore.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDateTime;

/**
 * @author AHMAD BUBA
 * Date:2/21/25
 * Time:16:11
 */

public record LogisticInDispatcherPossessionCommand(@TargetAggregateIdentifier String requestId,
                                                    LocalDateTime timestamp) {
    public LogisticInDispatcherPossessionCommand(final String requestId) {
        this(requestId, LocalDateTime.now());
    }
}
