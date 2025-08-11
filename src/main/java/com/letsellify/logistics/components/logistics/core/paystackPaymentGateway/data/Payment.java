package com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.data;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.dataMapper.PaystackDataMapper;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.database.entity.PaystackPaymentEntity;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.resource.PaystackPaymentInitializationResource;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

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

    // user email instead, consider adding role too
    private String userEmail;

    private LogisticAppRole userRole;


    public PaystackPaymentInitializationResource getResource() {
        return PaystackDataMapper.INSTANCE.dataToResource(this);
    }
}
