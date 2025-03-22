package com.letsellify.logistics.components.logistic.core.request.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.letsellify.logistics.components.logistic.core.request.data.LogisticsRequest;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:16:19
 */


@Getter
public class LogisticRequestBroadcast extends ApplicationEvent {
    String requestId;
    String vendorEmail;
    String vendorName;
    String vendorPhone;
    String itemName;
    String description;
    BigDecimal amountForShipping;
    BigDecimal amountForStorage;
    List<String> images;
    String currentState;
    String currentLga;
    String shippingState;
    String shippingLga;
    LocalDate possibleDeliveryDateStart;
    LocalDate possibleDeliveryDateEnd;

    public LogisticRequestBroadcast(final LogisticsRequest logisticsRequest) {
        super(logisticsRequest);
        this.requestId = logisticsRequest.getShippingRequestId();
        this.vendorEmail = logisticsRequest.getVendor().getVendorEmail();
        this.vendorName = logisticsRequest.getVendor().getVendorName();
        this.vendorPhone = logisticsRequest.getVendor().getVendorPhone();
        this.itemName = logisticsRequest.getItem().getItemName();
        this.description = logisticsRequest.getItem().getItemDescription();
        this.amountForShipping = logisticsRequest.getAmountForShipping();
        this.amountForStorage = logisticsRequest.getAmountForStorage();
        this.images = logisticsRequest.getImages();
        this.currentState = logisticsRequest.getCurrentState();
        this.currentLga = logisticsRequest.getCurrentLga();
        this.shippingState = logisticsRequest.getShippingState();
        this.shippingLga = logisticsRequest.getShippingLga();
        this.possibleDeliveryDateStart = logisticsRequest.getStartDate();
        this.possibleDeliveryDateEnd = logisticsRequest.getEndDate();
    }

}
