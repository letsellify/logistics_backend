package com.letsellify.logistics.components.logistics.commands;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.letsellify.logistics.components.logistics.core.paymentManagement.data.PaymentMethod;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsItemImage;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:10:15
 */


@Getter
public class LogisticsRequestCommand {
    @TargetAggregateIdentifier
    private final String requestId;
    private final String vendorEmail;
    private final String vendorFirstName;
    private final String vendorLastName;
    private final String vendorPhone;
    private final String itemName;
    private final String description;
    private final BigDecimal amountForShipping;
    private final BigDecimal amountForStorage;
    private final BigDecimal totalAmountAfterTax;
    private final PaymentMethod modeOfPayment;
    private final List<LogisticsItemImage> images;
    private final String currentState;
    private final String currentLga;
    private final String shippingState;
    private final String shippingLga;
    private final LocalDate possibleDeliveryDateStart;
    private final LocalDate possibleDeliveryDateEnd;

    public LogisticsRequestCommand(
      final String vendorEmail,
      final String vendorFirstName,
      final String vendorLastName,
      final String vendorPhone,
      final String itemName,
      final String description,
      final BigDecimal amountForShipping,
      final BigDecimal amountForStorage,
      final BigDecimal totalAmountAfterTax,
      final PaymentMethod modeOfPayment,
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
        this.vendorFirstName = vendorFirstName;
        this.vendorLastName = vendorLastName;
        this.vendorPhone = vendorPhone;
        this.itemName = itemName;
        this.description = description;
        this.amountForShipping = amountForShipping;
        this.amountForStorage = amountForStorage;
        this.totalAmountAfterTax = totalAmountAfterTax;
        this.modeOfPayment = modeOfPayment;
        this.images = images;
        this.currentState = currentState;
        this.currentLga = currentLga;
        this.shippingState = shippingState;
        this.shippingLga = shippingLga;
        this.possibleDeliveryDateStart = possibleDeliveryDateStart;
        this.possibleDeliveryDateEnd = possibleDeliveryDateEnd;
    }
}
