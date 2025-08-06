package com.letsellify.logistics.components.logistic.core.request.data;

import com.letsellify.logistics.components.logistic.core.request.dataMapper.LogisticRequestDataMapper;
import com.letsellify.logistics.components.logistic.core.request.database.entity.LogisticRequestEntity;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.LogisticRequestResource;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.VendorLogisticRequestResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:16:01
 */

@AllArgsConstructor
@Builder
@Getter
public final class LogisticRequest {

    private final UUID id;

    private final String shippingRequestId;

    private final UUID senderId;

    private final Item item;

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

    private final String pickUpState;

    private final String pickUpLga;

    private final String pickUpAddress;

    private final LocalDateTime requestDate;

    private final LogisticsStatus status;

//    public LogisticsRequest(final LogisticRequestEntity entity) {
//        this.id = entity.getId();
//        this.shippingRequestId = entity.getShippingRequestId();
//        this.item = entity.getItem();
//    }

    public LogisticRequest(final LogisticRequestEntity entity, final List<String> imagesPresignedUrl) {
        this.id = entity.getId();
        this.shippingRequestId = entity.getShippingRequestId();
        this.senderId = entity.getSenderId();
        this.item = new Item(entity.getItemEntity().getName(), entity.getItemEntity().getQuantity(), entity.getItemEntity().getDescription(), entity.getItemEntity().getFragility(), entity.getItemEntity().getWeight(), entity.getItemEntity().getConditions().stream().map(conditionEntity -> new ItemCondition(conditionEntity.getName())).collect(Collectors.toSet()));
        this.images = imagesPresignedUrl;
        this.receiverFullName = entity.getReceiver().getReceiverFullName();
        this.receiverLocation = entity.getReceiver().getReceiverLocation();
        this.receiverState = entity.getReceiver().getReceiverState();
        this.receiverLga = entity.getReceiver().getReceiverLga();
        this.receiverEmail = entity.getReceiver().getReceiverEmail();
        this.receiverCallNumber = entity.getReceiver().getReceiverCallNumber();
        this.receiverWhatsAppNumber = entity.getReceiver().getReceiverWhatsAppNumber();
        this.agentPay = entity.getAgentPay();
        this.dispatcherPay = entity.getDispatcherPay();
        this.totalSpendingAfterTax = entity.getTotalSpending();
        this.dispatcherPickUpDate = entity.getDispatcherPickUpDate();
        this.dispatcherDeliveryDate = entity.getDispatcherDeliveryDate();
        this.pickUpState = entity.getPickUpState();
        this.pickUpLga = entity.getPickUpLga();
        this.pickUpAddress = entity.getPickUpAddress();
        this.requestDate = entity.getRequestDate();
        this.status = entity.getStatus();
    }

    public LogisticRequestResource getResource() {
        return LogisticRequestDataMapper.INSTANCE.dataToResource(this);
    }

    public VendorLogisticRequestResource getVendorResource() {
        return LogisticRequestDataMapper.INSTANCE.dataToVendorResource(this);
    }

}
