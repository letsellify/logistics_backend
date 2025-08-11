package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource;

import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.LogisticsStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author AHMAD BUBA
 * Date:1/23/25
 * Time:08:04
 */


public record FullLogisticRequestResource(
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
        LogisticsStatus status,
        LogisticVendorResource sender,
        LogisticDispatcherResource dispatcher,
        LogisticAgentResource agent
) {

}
