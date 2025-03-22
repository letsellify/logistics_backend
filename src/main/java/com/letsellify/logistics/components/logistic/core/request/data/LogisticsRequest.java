package com.letsellify.logistics.components.logistic.core.request.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.letsellify.logistics.components.logistic.core.agent.data.LogisticAgent;
import com.letsellify.logistics.components.logistic.core.dispatcher.data.LogisticDispatcher;
import com.letsellify.logistics.components.logistic.core.request.dataMapper.LogisticRequestDataMapper;
import com.letsellify.logistics.components.logistic.core.request.database.entity.LogisticRequestEntity;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.LogisticRequestResource;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:16:01
 */

@Data
public class LogisticsRequest {
    private final LogisticRequestEntity entity;

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

    private LogisticDispatcher dispatcher;

    private LogisticsVendor vendor;

    private LogisticAgent agent;

    public LogisticsRequest(final LogisticRequestEntity entity) {
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
        this.dispatcher = entity.getDispatcher();
        this.vendor = entity.getVendor();
        this.agent = entity.getAgent();
    }

    public LogisticsRequest(final LogisticRequestEntity entity, final List<String> imagesPresignedUrl) {
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
        this.dispatcher = entity.getDispatcher();
        this.vendor = entity.getVendor();
        this.agent = entity.getAgent();
    }

    public LogisticRequestResource getResource() {
        return LogisticRequestDataMapper.INSTANCE.dataToResource(this);
    }

}
