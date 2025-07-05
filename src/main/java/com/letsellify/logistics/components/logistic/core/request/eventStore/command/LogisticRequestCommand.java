package com.letsellify.logistics.components.logistic.core.request.eventStore.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistic.core.request.data.Sender;

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
    private final String pickUpState;
    private final String pickUpLga;
    private final String pickUpAddress;
    private final LocalDateTime requestDate;

    public LogisticRequestCommand(
      final Sender sender,
      final String itemName,
      final int quantity,
      final String description,
      final String fragility,
      final Set<String> condition,
      final int weight,
      final List<LogisticsItemImage> images,
      final String receiverFullName,
      final String receiverLocation,
      final String receiverState,
      final String receiverLga,
      final String receiverEmail,
      final String receiverCallPhoneNumber,
      final String receiverWhatsAppPhoneNumber,
      final BigDecimal agentPay,
      final BigDecimal dispatcherPay,
      final BigDecimal totalSpendingAfterTax,
      final LocalDate dispatcherPickUpDate,
      final LocalDate dispatcherDeliveryDate,
      final String pickUpState,
      final String pickUpLga,
      final String pickUpAddress
    ) {
        this.requestId = UUID.randomUUID().toString();
        this.sender = sender;
        this.itemName = itemName;
        this.quantity = quantity;
        this.description = description;
        this.fragility = fragility;
        this.condition = condition;
        this.weight = weight;
        this.images = images;
        this.receiverFullName = receiverFullName;
        this.receiverLocation = receiverLocation;
        this.receiverState = receiverState;
        this.receiverLga = receiverLga;
        this.receiverEmail = receiverEmail;
        this.receiverCallNumber = receiverCallPhoneNumber;
        this.receiverWhatsAppNumber = receiverWhatsAppPhoneNumber;
        this.agentPay = agentPay;
        this.dispatcherPay = dispatcherPay;
        this.totalSpendingAfterTax = totalSpendingAfterTax;
        this.dispatcherPickUpDate = dispatcherPickUpDate;
        this.dispatcherDeliveryDate = dispatcherDeliveryDate;
        this.pickUpState = pickUpState;
        this.pickUpLga = pickUpLga;
        this.pickUpAddress = pickUpAddress;
        this.requestDate = LocalDateTime.now();
    }
}
