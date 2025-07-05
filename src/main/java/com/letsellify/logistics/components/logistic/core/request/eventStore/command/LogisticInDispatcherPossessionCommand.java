package com.letsellify.logistics.components.logistic.core.request.eventStore.command;

import java.time.LocalDateTime;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * @author AHMAD BUBA
 * Date:2/21/25
 * Time:16:11
 */

public record LogisticInDispatcherPossessionCommand(@TargetAggregateIdentifier String requestId, LocalDateTime timestamp) {
    public LogisticInDispatcherPossessionCommand(final String requestId) {
        this(requestId, LocalDateTime.now());
    }
}
