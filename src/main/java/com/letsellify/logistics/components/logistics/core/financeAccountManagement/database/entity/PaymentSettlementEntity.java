package com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.letsellify.logistics.common.audit.entity.Auditable;
import com.letsellify.logistics.common.data.LogisticsAppRole;

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
    private String userEmail;
    @Enumerated(EnumType.STRING)
    private LogisticsAppRole appRole;
    private BigDecimal amount;


    protected PaymentSettlementEntity() {
        super();
    }

    public static PaymentSettlementEntity getInstance(final String userEmail, final LogisticsAppRole appRole, final BigDecimal amount) {
        final PaymentSettlementEntity paymentSettlement = new PaymentSettlementEntity();
        paymentSettlement.settlementId = UUID.randomUUID();
        paymentSettlement.userEmail = userEmail;
        paymentSettlement.appRole = appRole;
        paymentSettlement.amount = amount;
        return paymentSettlement;
    }
}
