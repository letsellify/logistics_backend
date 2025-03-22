package com.letsellify.logistics.components.logistic.core.request.eventStore.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItemImage;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:10:17
 */


@AllArgsConstructor
@Getter
public class LogisticRequestedEvent {
    private String requestId;
    private String vendorEmail;
    private String vendorName;
    private String vendorPhone;
    private String itemName;
    private String description;
    private BigDecimal amountForShipping;
    private BigDecimal amountForStorage;
    private BigDecimal totalAmountAfterTax;
    private List<LogisticsItemImage>images;
    private String currentState;
    private String currentLga;
    private String shippingState;
    private String shippingLga;
    private LocalDate possibleDeliveryDateStart;
    private LocalDate possibleDeliveryDateEnd;
}
