package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.LogisticsDispatcher;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsAgent;

/**
 * @author AHMAD BUBA
 * Date:1/29/25
 * Time:18:05
 */


public record LogisticsSettlementEvent(
  String requestId,
  LogisticsAgent agent,
  LogisticsDispatcher dispatcher,
  BigDecimal amountForAgent,
  BigDecimal amountForDispatcher,
  LocalDateTime timestamp
) {
}
