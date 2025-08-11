package com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.database.entity;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

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

    private UUID userId;

    @Enumerated(EnumType.STRING)
    private LogisticAppRole userRole;

    private boolean success;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "charge_success_webhook_data_id")
    private PaystackChargeSuccessWebhookEntity chargeSuccessWebhookData;

    protected PaystackPaymentEntity() {
        super();
    }

    public static PaystackPaymentEntity create(final UUID userId, final LogisticAppRole userRole, final BigDecimal amount, final boolean status, final String message, final String authorizationUrl, final String accessCode, final String reference) {
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(userId, "UserEmail must not be null");
        Objects.requireNonNull(userRole, "UserRole must not be null");
        final PaystackPaymentEntity paymentEntity = new PaystackPaymentEntity();
        paymentEntity.id = UUID.randomUUID();
        paymentEntity.userId = userId;
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
