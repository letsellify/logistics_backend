package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.LogisticsDispatcher;

/**
 * @author AHMAD BUBA
 * Date:1/29/25
 * Time:18:03
 */

public record AcceptDispatchRequestCommand(
  @TargetAggregateIdentifier String requestId,
  LogisticsDispatcher dispatcher
) {}
