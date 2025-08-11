package com.letsellify.logistics.components.logistics.core.financeAccountManagement.data;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity.LogisticsAccountTransactionEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:10:33
 */

@Data
public class LogisticsAccountTransaction {
    private UUID userId;
    private LogisticAppRole appRole;
    private UUID transactionId;
    private LogisticsAccountTransactionType transactionType;
    private BigDecimal amount;

    public LogisticsAccountTransaction(final LogisticsAccountTransactionEntity logisticsAccountTransactionEntity) {
        this.transactionType = logisticsAccountTransactionEntity.getTransactionType();
        this.amount = logisticsAccountTransactionEntity.getAmount();
        this.transactionId = logisticsAccountTransactionEntity.getId();
        this.userId = logisticsAccountTransactionEntity.getLogisticsAccount().getUserId();
        this.appRole = logisticsAccountTransactionEntity.getLogisticsAccount().getAppRole();
    }

}
