package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.eventStore.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/29/25
 * Time:18:05
 */


public record LogisticSettlementEvent(
        String requestId,
        UUID agentId,
        UUID dispatcherId,
        BigDecimal amountForAgent,
        BigDecimal amountForDispatcher,
        LocalDateTime timestamp
) {
}
