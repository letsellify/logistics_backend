package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.eventStore.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/29/25
 * Time:18:04
 */

public record AcceptStorageRequestCommand(
        @TargetAggregateIdentifier String requestId,
        UUID agentId
) {
}
