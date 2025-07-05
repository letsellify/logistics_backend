package com.letsellify.logistics.components.logistic.core.request.eventStore.command;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * @author AHMAD BUBA
 * Date:1/29/25
 * Time:18:04
 */

public record AcceptStorageRequestCommand(
  @TargetAggregateIdentifier String requestId,
  UUID agentId
) {}
