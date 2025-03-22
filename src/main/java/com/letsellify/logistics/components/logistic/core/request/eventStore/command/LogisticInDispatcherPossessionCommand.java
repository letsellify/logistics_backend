package com.letsellify.logistics.components.logistic.core.request.eventStore.command;

import java.time.Instant;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * @author AHMAD BUBA
 * Date:2/21/25
 * Time:16:11
 */

public record LogisticInDispatcherPossessionCommand(@TargetAggregateIdentifier String requestId, Instant timestamp) {
    public LogisticInDispatcherPossessionCommand(final String requestId) {
        this(requestId, Instant.now());
    }
}
