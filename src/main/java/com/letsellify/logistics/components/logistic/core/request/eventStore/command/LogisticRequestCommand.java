package com.letsellify.logistics.components.logistic.core.request.eventStore.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItemImage;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:10:15
 */


@Getter
public class LogisticRequestCommand {
    @TargetAggregateIdentifier
    private final String requestId;
    private final String vendorEmail;
    private final String vendorName;
    private final String vendorPhone;
    private final String itemName;
    private final String description;
    private final BigDecimal amountForShipping;
    private final BigDecimal amountForStorage;
    private final BigDecimal totalAmountAfterTax;
    private final List<LogisticsItemImage> images;
    private final String currentState;
    private final String currentLga;
    private final String shippingState;
    private final String shippingLga;
    private final LocalDate possibleDeliveryDateStart;
    private final LocalDate possibleDeliveryDateEnd;

    public LogisticRequestCommand(
      final String vendorEmail,
      final String vendorName,
      final String vendorPhone,
      final String itemName,
      final String description,
      final BigDecimal amountForShipping,
      final BigDecimal amountForStorage,
      final BigDecimal totalAmountAfterTax,
      final List<LogisticsItemImage> images,
      final String currentState,
      final String currentLga,
      final String shippingState,
      final String shippingLga,
      final LocalDate possibleDeliveryDateStart,
      final LocalDate possibleDeliveryDateEnd
    ) {
        this.requestId = UUID.randomUUID().toString();
        this.vendorEmail = vendorEmail;
        this.vendorName = vendorName;
        this.vendorPhone = vendorPhone;
        this.itemName = itemName;
        this.description = description;
        this.amountForShipping = amountForShipping;
        this.amountForStorage = amountForStorage;
        this.totalAmountAfterTax = totalAmountAfterTax;
        this.images = images;
        this.currentState = currentState;
        this.currentLga = currentLga;
        this.shippingState = shippingState;
        this.shippingLga = shippingLga;
        this.possibleDeliveryDateStart = possibleDeliveryDateStart;
        this.possibleDeliveryDateEnd = possibleDeliveryDateEnd;
    }
}
