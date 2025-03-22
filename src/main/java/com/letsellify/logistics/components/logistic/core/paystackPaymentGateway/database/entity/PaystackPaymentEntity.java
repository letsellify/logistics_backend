package com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.database.entity;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.common.entityAudit.entity.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:16:13
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class PaystackPaymentEntity extends Auditable {
    @Id
    private UUID id;

    private BigDecimal amount;

    private boolean initializationStatus;

    private String initializationMessage;

    private String authorizationUrl;

    @NaturalId
    private String accessCode;

    private String reference;

    private String userEmail;

    @Enumerated(EnumType.STRING)
    private LogisticAppRole userRole;

    private boolean success;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "charge_success_webhook_data_id")
    private PaystackChargeSuccessWebhookEntity chargeSuccessWebhookData;

    protected PaystackPaymentEntity() {
        super();
    }

    public static PaystackPaymentEntity create(final String userEmail, final LogisticAppRole userRole, final BigDecimal amount, final boolean status, final String message, final String authorizationUrl, final String accessCode, final String reference) {
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(userEmail, "UserEmail must not be null");
        Objects.requireNonNull(userRole, "UserRole must not be null");
        final PaystackPaymentEntity paymentEntity = new PaystackPaymentEntity();
        paymentEntity.id = UUID.randomUUID();
        paymentEntity.userEmail = userEmail;
        paymentEntity.userRole = userRole;
        paymentEntity.amount = amount;
        paymentEntity.initializationStatus = status;
        paymentEntity.initializationMessage = message;
        paymentEntity.authorizationUrl = authorizationUrl;
        paymentEntity.accessCode = accessCode;
        paymentEntity.reference = reference;
        paymentEntity.success = false;
        return paymentEntity;
    }

    public void transactionSuccess(final PaystackChargeSuccessWebhookEntity successWebhookEntity) {
        this.chargeSuccessWebhookData = successWebhookEntity;
        this.success = true;
    }
}
