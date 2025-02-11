package com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.database.entity;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import com.letsellify.logistics.common.audit.entity.Auditable;
import com.letsellify.logistics.common.data.LogisticsAppRole;

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
@EqualsAndHashCode(callSuper = true)
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
    private LogisticsAppRole userRole;

    private boolean success;

    @OneToOne
    @JoinColumn(name = "charge_success_webhook_data_id")
    private PaystackChargeSuccessWebhookEntity chargeSuccessWebhookData;

    protected PaystackPaymentEntity() {
        super();
    }

    public static PaystackPaymentEntity getInstance(final String userEmail, final LogisticsAppRole userRole, final BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(userEmail, "UserEmail must not be null");
        Objects.requireNonNull(userRole, "UserRole must not be null");
        final PaystackPaymentEntity paymentEntity = new PaystackPaymentEntity();
        paymentEntity.id = UUID.randomUUID();
        paymentEntity.amount = amount;
        paymentEntity.initializationStatus = false;
        paymentEntity.userEmail = userEmail;
        paymentEntity.userRole = userRole;
        paymentEntity.success = false;
        return paymentEntity;
    }
}
