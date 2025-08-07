package com.letsellify.logistics.components.logistic.core.request.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.letsellify.logistics.components.logistic.core.request.data.ItemCondition;
import org.springframework.context.ApplicationEvent;

import com.letsellify.logistics.components.logistic.core.request.data.LogisticRequest;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:16:19
 */


@Getter
public class LogisticRequestBroadcast extends ApplicationEvent {
    private final String requestId;
    private final UUID senderId;
    private final String itemName;
    private final String itemDescription;
    private final String itemFragility;
    private final int itemWeight;
    private final Set<String> condition;
    private final List<String> images;
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

    public LogisticRequestBroadcast(final LogisticRequest logisticsRequest) {
        super(logisticsRequest);
        this.requestId = logisticsRequest.getShippingRequestId();
        this.senderId = logisticsRequest.getSenderId();
        this.itemName = logisticsRequest.getItem().name();
        this.itemDescription = logisticsRequest.getItem().description();
        this.itemFragility = logisticsRequest.getItem().fragility();
        this.itemWeight = logisticsRequest.getItem().weight();
        this.condition = logisticsRequest.getItem().conditions().stream().map(ItemCondition::name).collect(Collectors.toSet());
        this.agentPay = logisticsRequest.getAgentPay();
        this.dispatcherPay = logisticsRequest.getDispatcherPay();
        this.receiverEmail = logisticsRequest.getReceiverEmail();
        this.receiverFullName = logisticsRequest.getReceiverFullName();
        this.receiverCallNumber = logisticsRequest.getReceiverCallNumber();
        this.receiverWhatsAppNumber = logisticsRequest.getReceiverWhatsAppNumber();
        this.receiverState = logisticsRequest.getReceiverState();
        this.receiverLga = logisticsRequest.getReceiverLga();
        this.receiverLocation = logisticsRequest.getReceiverLocation();
        this.images = logisticsRequest.getImages();
        this.totalSpendingAfterTax = logisticsRequest.getTotalSpendingAfterTax();
        this.dispatcherPickUpDate = logisticsRequest.getDispatcherPickUpDate();
        this.dispatcherDeliveryDate = logisticsRequest.getDispatcherDeliveryDate();
        this.agentReceivingDate = logisticsRequest.getAgentReceivingDate();
        this.agentDeliveringDate = logisticsRequest.getAgentDeliveringDate();
        this.pickUpState = logisticsRequest.getPickUpState();
        this.pickUpLga = logisticsRequest.getPickUpLga();
        this.pickUpAddress = logisticsRequest.getPickUpAddress();
        this.requestDate = logisticsRequest.getRequestDate();
    }

}
