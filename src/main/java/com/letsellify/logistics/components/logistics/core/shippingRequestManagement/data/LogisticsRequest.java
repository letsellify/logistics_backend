package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.LogisticsDispatcher;
import com.letsellify.logistics.components.logistics.core.paymentManagement.data.PaymentMethod;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.dataMapper.ShippingRequestDataMapper;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.database.entity.LogisticsRequestEntity;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.rest.resource.LogisticsRequestResource;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:16:01
 */

@Data
public class LogisticsRequest {
    private final LogisticsRequestEntity entity;

    private UUID id;

    private String shippingRequestId;

    private LogisticsItem item;

    private List<String> images;

    private String currentState;

    private String currentLga;

    private String shippingState;

    private String shippingLga;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal amountForShipping;

    private BigDecimal amountForStorage;

    private PaymentMethod modeOfPayment;

    private LogisticsDispatcher dispatcher;

    private LogisticsVendor vendor;

    private LogisticsAgent agent;

    public LogisticsRequest(final LogisticsRequestEntity entity) {
        this.entity = entity;
        this.id = entity.getId();
        this.shippingRequestId = entity.getShippingRequestId();
        this.item = entity.getItem();
        this.currentState = entity.getCurrentState();
        this.currentLga = entity.getCurrentLga();
        this.shippingState = entity.getShippingState();
        this.shippingLga = entity.getShippingLga();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.amountForShipping = entity.getAmountForShipping();
        this.amountForStorage = entity.getAmountForStorage();
        this.modeOfPayment = entity.getModeOfPayment();
        this.dispatcher = entity.getDispatcher();
        this.vendor = entity.getVendor();
        this.agent = entity.getAgent();
    }

    public LogisticsRequest(final LogisticsRequestEntity entity, final List<String> imagesPresignedUrl) {
        this.entity = entity;
        this.id = entity.getId();
        this.shippingRequestId = entity.getShippingRequestId();
        this.item = new LogisticsItem(entity.getItem().getItemName(),entity.getItem().getItemDescription());
        this.images = imagesPresignedUrl;
        this.currentState = entity.getCurrentState();
        this.currentLga = entity.getCurrentLga();
        this.shippingState = entity.getShippingState();
        this.shippingLga = entity.getShippingLga();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.amountForShipping = entity.getAmountForShipping();
        this.amountForStorage = entity.getAmountForStorage();
        this.modeOfPayment = entity.getModeOfPayment();
        this.dispatcher = entity.getDispatcher();
        this.vendor = entity.getVendor();
        this.agent = entity.getAgent();
    }

    public LogisticsRequestResource getResource() {
        return ShippingRequestDataMapper.INSTANCE.dataToResource(this);
    }

}
