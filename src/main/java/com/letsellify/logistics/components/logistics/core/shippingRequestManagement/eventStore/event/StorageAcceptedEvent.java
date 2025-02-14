package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.event;

import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsAgent;

/**
 * @author AHMAD BUBA
 * Date:2/7/25
 * Time:07:04
 */

public record StorageAcceptedEvent(
  String requestId,
  LogisticsAgent agent
) {}
