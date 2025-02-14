package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsAgent;

/**
 * @author AHMAD BUBA
 * Date:1/29/25
 * Time:18:04
 */

public record AcceptStorageRequestCommand(
  @TargetAggregateIdentifier String requestId,
  LogisticsAgent agent
) {}
