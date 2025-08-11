package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author AHMAD BUBA
 * Date:3/13/25
 * Time:14:34
 */


@Builder
public record Request(
        String itemName,
        String description,
        BigDecimal amountForShipping,
        BigDecimal amountForStorage,
        List<String> images,
        String currentState,
        String currentLga,
        String shippingState,
        String shippingLga,
        LocalDate possibleDeliveryDateStart,
        LocalDate possibleDeliveryDateEnd
) {
//    public LogisticRequestResource getResource() {
//        return LogisticRequestDataMapper.INSTANCE.dataToResource(this);
//    }
}
