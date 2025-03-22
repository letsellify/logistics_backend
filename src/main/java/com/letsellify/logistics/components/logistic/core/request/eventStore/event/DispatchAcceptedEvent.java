package com.letsellify.logistics.components.logistic.core.request.eventStore.event;

import com.letsellify.logistics.components.logistic.core.dispatcher.data.LogisticDispatcher;

/**
 * @author AHMAD BUBA
 * Date:2/6/25
 * Time:09:30
 */

public record DispatchAcceptedEvent(
  String requestId,
  LogisticDispatcher dispatcher
) {}
