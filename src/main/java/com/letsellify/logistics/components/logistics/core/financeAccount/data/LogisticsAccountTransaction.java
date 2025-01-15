package com.letsellify.logistics.components.logistics.core.financeAccount.data;

import java.math.BigDecimal;

import com.letsellify.logistics.components.logistics.core.financeAccount.database.entity.LogisticsAccountTransactionEntity;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:10:33
 */

@Data
public class LogisticsAccountTransaction {
    private LogisticsAccountTransactionType transactionType;
    private BigDecimal amount;

    public LogisticsAccountTransaction(final LogisticsAccountTransactionEntity logisticsAccountTransactionEntity) {
        this.transactionType = logisticsAccountTransactionEntity.getTransactionType();
        this.amount = logisticsAccountTransactionEntity.getAmount();
    }

}
