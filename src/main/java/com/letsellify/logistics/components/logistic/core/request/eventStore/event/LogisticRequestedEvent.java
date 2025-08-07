package com.letsellify.logistics.components.logistic.core.request.eventStore.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistic.core.request.data.Sender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:10:17
 */


@AllArgsConstructor
@Builder
@Getter
public final class LogisticRequestedEvent {
    private final String requestId;

    private final Sender sender;

    private final String itemName;

    private final int quantity;

    private final String description;

    private final String fragility;

    private final Set<String> condition;

    private final int weight;

    private final List<LogisticsItemImage> images;

    private final String receiverFullName;

    private final String receiverLocation;

    private final String receiverState;

    private final String receiverLga;

    private final String receiverEmail;

    private final String receiverCallNumber;

    private final String receiverWhatsAppNumber;

    private final BigDecimal agentPay;

    private final BigDecimal dispatcherPay;

    private final BigDecimal totalSpendingAfterTax;

    private final LocalDate dispatcherPickUpDate;

    private final LocalDate dispatcherDeliveryDate;

    private final LocalDate agentReceivingDate;

    private final LocalDate agentDeliveringDate;

    private final String pickUpState;

    private final String pickUpLga;

    private final String pickUpAddress;

    private final LocalDateTime requestDate;
}
