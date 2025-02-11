package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.events;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.LogisticsDispatcher;

/**
 * @author AHMAD BUBA
 * Date:2/6/25
 * Time:09:30
 */

public record DispatchAcceptedEvent(
  String requestId,
  LogisticsDispatcher dispatcher
) {}
