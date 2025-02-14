package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.letsellify.logistics.components.logistics.core.paymentManagement.data.PaymentMethod;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsRequest;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:16:19
 */


@Getter
public class ShippingRequestBroadcast extends ApplicationEvent {
    String requestId;
    String vendorEmail;
    String vendorFirstName;
    String vendorLastName;
    String vendorPhone;
    String itemName;
    String description;
    BigDecimal amountForShipping;
    BigDecimal amountForStorage;
    PaymentMethod modeOfPayment;
    List<String> images;
    String currentState;
    String currentLga;
    String shippingState;
    String shippingLga;
    LocalDate possibleDeliveryDateStart;
    LocalDate possibleDeliveryDateEnd;

    public ShippingRequestBroadcast(final LogisticsRequest logisticsRequest) {
        super(logisticsRequest);
        this.requestId = logisticsRequest.getShippingRequestId();
        this.vendorEmail = logisticsRequest.getVendor().getVendorEmail();
        this.vendorFirstName = logisticsRequest.getVendor().getVendorFirstName();
        this.vendorLastName = logisticsRequest.getVendor().getVendorLastName();
        this.vendorPhone = logisticsRequest.getVendor().getVendorPhone();
        this.itemName = logisticsRequest.getItem().getItemName();
        this.description = logisticsRequest.getItem().getItemDescription();
        this.amountForShipping = logisticsRequest.getAmountForShipping();
        this.amountForStorage = logisticsRequest.getAmountForStorage();
        this.modeOfPayment = logisticsRequest.getModeOfPayment();
        this.images = logisticsRequest.getImages();
        this.currentState = logisticsRequest.getCurrentState();
        this.currentLga = logisticsRequest.getCurrentLga();
        this.shippingState = logisticsRequest.getShippingState();
        this.shippingLga = logisticsRequest.getShippingLga();
        this.possibleDeliveryDateStart = logisticsRequest.getStartDate();
        this.possibleDeliveryDateEnd = logisticsRequest.getEndDate();
    }

}
