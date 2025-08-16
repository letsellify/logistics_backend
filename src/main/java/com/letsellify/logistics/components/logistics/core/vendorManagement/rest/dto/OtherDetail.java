package com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author AHMAD BUBA
 * Date:6/21/25
 * Time:18:00
 */

public record OtherDetail(
        BigDecimal agentPay,
        BigDecimal dispatcherPay,
        LocalDate dispatcherPickUpDate,
        LocalDate dispatcherDeliveryDate,
        String pickUpState,
        String pickUpLga,
        String pickUpAddress
) {
}
