package com.letsellify.logistics.components.logistic.core.request.rest.resource;


import com.letsellify.logistics.components.logistic.core.request.data.LogisticsStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Author: Ahmad Buba
 * Date:8/6/25
 */


public record VendorLogisticRequestResource(
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
        String pickUpState,
        String pickUpLga,
        String pickUpAddress,
        LocalDateTime requestDate,
        LogisticsStatus status,
        int totalLogisticRequests
) {
}
