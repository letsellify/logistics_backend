package com.letsellify.logistics.components.logistics.core.financeAccountManagement.data;

import java.math.BigDecimal;
import java.util.UUID;

import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity.LogisticsAccountTransactionEntity;
import com.letsellify.logistics.common.data.LogisticsAppRole;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:10:33
 */

@Data
public class LogisticsAccountTransaction {
    private String userEmail;
    private LogisticsAppRole appRole;
    private UUID transactionId;
    private LogisticsAccountTransactionType transactionType;
    private BigDecimal amount;

    public LogisticsAccountTransaction(final LogisticsAccountTransactionEntity logisticsAccountTransactionEntity) {
        this.transactionType = logisticsAccountTransactionEntity.getTransactionType();
        this.amount = logisticsAccountTransactionEntity.getAmount();
        this.transactionId = logisticsAccountTransactionEntity.getId();
        this.userEmail = logisticsAccountTransactionEntity.getAccount().getUserEmail();
        this.appRole = logisticsAccountTransactionEntity.getAccount().getAppRole();
    }

}
