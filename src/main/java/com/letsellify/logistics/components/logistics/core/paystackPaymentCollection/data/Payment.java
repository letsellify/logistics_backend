package com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.data;

import java.math.BigDecimal;
import java.util.UUID;

import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.dataMapper.PaystackDataMapper;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.database.entity.PaystackPaymentEntity;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.resource.PaystackPaymentInitializationResource;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:21:26
 */

@Data
public class Payment {
    private final PaystackPaymentEntity entity;

    private UUID id;

    private BigDecimal amount;

    private boolean initializationStatus;

    private String initializationMessage;

    private String authorizationUrl;

    private String accessCode;

    private String reference;

    private UUID userId;


    public PaystackPaymentInitializationResource getResource() {
        return PaystackDataMapper.INSTANCE.dataToResource(this);
    }
}
