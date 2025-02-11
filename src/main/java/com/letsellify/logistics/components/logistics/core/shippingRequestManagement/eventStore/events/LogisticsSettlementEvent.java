package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.LogisticsDispatcher;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsAgent;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/29/25
 * Time:18:05
 */

@Getter
public class LogisticsSettlementEvent {
    private final String requestId;
    private final LogisticsDispatcher dispatcher;
    private final LogisticsAgent agent;
    private final BigDecimal amountForAgent;
    private final BigDecimal amountForDispatcher;
    private final LocalDateTime timestamp;

    public LogisticsSettlementEvent(final String requestId, final LogisticsAgent agent, final LogisticsDispatcher dispatcher, final BigDecimal amountForAgent, final BigDecimal amountForDispatcher, final LocalDateTime timestamp) {
        this.requestId = requestId;
        this.agent = agent;
        this.dispatcher = dispatcher;
        this.amountForAgent = amountForAgent;
        this.amountForDispatcher = amountForDispatcher;
        this.timestamp = timestamp;
    }

}
