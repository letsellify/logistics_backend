package com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.letsellify.logistics.common.audit.entity.Auditable;
import com.letsellify.logistics.common.data.LogisticsAppRole;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.data.LogisticsAccountTransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:22:52
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class LogisticsAccountTransactionEntity extends Auditable {
    @Id
    private UUID id;
    private BigDecimal amount;
    private LogisticsAccountTransactionType transactionType;
    private UUID shippingId;
    @ManyToOne
    private LogisticsAccountEntity account;

    protected LogisticsAccountTransactionEntity() {
        super();
    }

    protected static LogisticsAccountTransactionEntity getInstance(final LogisticsAccountEntity account, final LogisticsAccountTransactionType transactionType, final BigDecimal amount, final UUID shippingId) {
        if (transactionType == LogisticsAccountTransactionType.DEBIT
            && account.getAppRole() == LogisticsAppRole.VENDOR
            && shippingId == null) {
            throw new IllegalArgumentException("Shipping ID must be provided for DEBIT transactions by VENDOR");
        }
        final LogisticsAccountTransactionEntity entity = new LogisticsAccountTransactionEntity();
        entity.id = UUID.randomUUID();
        entity.account = account;
        entity.transactionType = transactionType;
        entity.amount = amount;
        entity.shippingId = shippingId;
        return entity;
    }
}
