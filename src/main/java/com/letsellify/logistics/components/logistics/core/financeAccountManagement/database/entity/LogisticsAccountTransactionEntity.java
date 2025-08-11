package com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.data.LogisticsAccountTransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

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
    private String shippingId;
    @ManyToOne
    @JoinColumn(name = "logistics_account_id", nullable = false)
    private LogisticsAccountEntity logisticsAccount;

    protected LogisticsAccountTransactionEntity() {
        super();
    }

    protected static LogisticsAccountTransactionEntity getInstance(final LogisticsAccountEntity account, final LogisticsAccountTransactionType transactionType, final BigDecimal amount, final String shippingId) {
        if (transactionType == LogisticsAccountTransactionType.DEBIT
                && account.getAppRole() == LogisticAppRole.VENDOR
                && shippingId == null) {
            throw new IllegalArgumentException("Shipping ID must be provided for DEBIT transactions by VENDOR");
        }
        final LogisticsAccountTransactionEntity entity = new LogisticsAccountTransactionEntity();
        entity.id = UUID.randomUUID();
        entity.logisticsAccount = account;
        entity.transactionType = transactionType;
        entity.amount = amount;
        entity.shippingId = shippingId;
        return entity;
    }
}
