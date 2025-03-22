package com.letsellify.logistics.components.logistic.core.request.eventStore.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.letsellify.logistics.components.logistic.core.agent.data.LogisticAgent;

/**
 * @author AHMAD BUBA
 * Date:1/29/25
 * Time:18:04
 */

public record AcceptStorageRequestCommand(
  @TargetAggregateIdentifier String requestId,
  LogisticAgent agent
) {}
