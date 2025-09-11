package com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity;


import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.data.LogisticsAccountTransactionType;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.data.PaystackPayment;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: Ahmad Buba
 * Date: 8/27/25
 */

@Entity
@Getter
public class PaystackTransactionEntity extends Auditable {
    @Id
    private String id;

    private UUID paystackTransactionId;

    private UUID userId;

    private BigDecimal amount;

    private boolean initializationStatus;

    private String initializationMessage;

    private String authorizationUrl;

    private String accessCode;

    private String reference;

    @Enumerated(EnumType.STRING)
    private LogisticAppRole userRole;


    @Enumerated(EnumType.STRING)
    private LogisticsAccountTransactionType transactionType;

    protected PaystackTransactionEntity() {
        super();
    }

    public PaystackTransactionEntity(PaystackPayment paystackPayment, LogisticsAccountTransactionType transactionType) {
        super();
        this.id = UUID.randomUUID().toString();
        this.paystackTransactionId = paystackPayment.getId();
        this.userId = paystackPayment.getUserId();
        this.amount = paystackPayment.getAmount();
        this.initializationStatus = paystackPayment.isInitializationStatus();
        this.initializationMessage = paystackPayment.getInitializationMessage();
        this.authorizationUrl = paystackPayment.getAuthorizationUrl();
        this.accessCode = paystackPayment.getAccessCode();
        this.reference = paystackPayment.getReference();
        this.userRole = paystackPayment.getUserRole();
        this.transactionType = transactionType;
    }
}
