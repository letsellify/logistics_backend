package com.letsellify.logistics.components.logistic.core.request.eventStore.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.letsellify.logistics.components.logistic.core.dispatcher.data.LogisticDispatcher;
import com.letsellify.logistics.components.logistic.core.agent.data.LogisticAgent;

/**
 * @author AHMAD BUBA
 * Date:1/29/25
 * Time:18:05
 */


public record LogisticSettlementEvent(
  String requestId,
  LogisticAgent agent,
  LogisticDispatcher dispatcher,
  BigDecimal amountForAgent,
  BigDecimal amountForDispatcher,
  LocalDateTime timestamp
) {
}
