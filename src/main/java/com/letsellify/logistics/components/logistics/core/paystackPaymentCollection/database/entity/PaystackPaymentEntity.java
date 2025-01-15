package com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.database.entity;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import com.letsellify.logistics.components.logistics.core.auditing.database.entity.Auditable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:16:13
 */

@Entity
@Data
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

    private UUID userId;

    private boolean success;

    @OneToOne
    @JoinColumn(name = "charge_success_webhook_data_id")
    private PaystackChargeSuccessWebhookEntity chargeSuccessWebhookData;

    protected PaystackPaymentEntity() {
        super();
    }

    public static PaystackPaymentEntity getInstance(final BigDecimal amount, final UUID userId) {
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(userId, "UserId must not be null");
        final PaystackPaymentEntity paymentEntity = new PaystackPaymentEntity();
        paymentEntity.id = UUID.randomUUID();
        paymentEntity.amount = amount;
        paymentEntity.initializationStatus = false;
        paymentEntity.userId = userId;
        paymentEntity.success = false;
        return paymentEntity;
    }
}
