package com.letsellify.logistics.components.logistic.core.request.eventStore.command;

import java.time.LocalDateTime;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * @author AHMAD BUBA
 * Date:1/29/25
 * Time:18:03
 */

public record DispatchRequestAcceptedCommand(
  @TargetAggregateIdentifier
  String requestId,
  UUID dispatcherId,
  LocalDateTime accepted
) {
  public DispatchRequestAcceptedCommand(final String requestId, final UUID dispatcherId) {
    this(requestId,dispatcherId,LocalDateTime.now());
  }
}
