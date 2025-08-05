package com.letsellify.logistics.components.logistic.core.financeAccount.database.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import com.letsellify.logistics.common.data.LogisticAppRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:1/26/25
 * Time:09:07
 */

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class PaymentSettlementEntity extends Auditable {
    @Id
    private UUID settlementId;
    private UUID userId;
    @Enumerated(EnumType.STRING)
    private LogisticAppRole appRole;
    private BigDecimal amount;


    protected PaymentSettlementEntity() {
        super();
    }

    public static PaymentSettlementEntity getInstance(final UUID userId, final LogisticAppRole appRole, final BigDecimal amount) {
        final PaymentSettlementEntity paymentSettlement = new PaymentSettlementEntity();
        paymentSettlement.settlementId = UUID.randomUUID();
        paymentSettlement.userId = userId;
        paymentSettlement.appRole = appRole;
        paymentSettlement.amount = amount;
        return paymentSettlement;
    }
}
