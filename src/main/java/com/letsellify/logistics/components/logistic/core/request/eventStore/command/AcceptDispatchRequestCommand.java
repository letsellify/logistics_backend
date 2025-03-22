package com.letsellify.logistics.components.logistic.core.request.eventStore.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.letsellify.logistics.components.logistic.core.dispatcher.data.LogisticDispatcher;

/**
 * @author AHMAD BUBA
 * Date:1/29/25
 * Time:18:03
 */

public record AcceptDispatchRequestCommand(
  @TargetAggregateIdentifier String requestId,
  LogisticDispatcher dispatcher
) {}
