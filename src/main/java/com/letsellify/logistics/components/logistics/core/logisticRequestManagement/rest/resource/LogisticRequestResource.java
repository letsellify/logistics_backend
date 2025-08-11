package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource;


import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.LogisticsStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Ahmad Buba
 * Date:8/6/25
 */


public record LogisticRequestResource(
        String shippingRequestId,
        LogisticItemResource item,
        List<String> images,
        String receiverFullName,
        String receiverLocation,
        String receiverState,
        String receiverLga,
        String receiverEmail,
        String receiverCallNumber,
        String receiverWhatsAppNumber,
        BigDecimal agentPay,
        BigDecimal dispatcherPay,
        BigDecimal totalSpendingAfterTax,
        LocalDate dispatcherPickUpDate,
        LocalDate dispatcherDeliveryDate,
        LocalDate agentReceivingDate,
        LocalDate agentDeliveryDate,
        String pickUpState,
        String pickUpLga,
        String pickUpAddress,
        LocalDateTime requestDate,
        LogisticsStatus status
) {
}
