package com.letsellify.logistics.components.logistics.core.financeAccount.database.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.letsellify.logistics.components.logistics.core.auditing.database.entity.Auditable;
import com.letsellify.logistics.components.logistics.core.financeAccount.data.LogisticsAccountTransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:22:52
 */

@Entity
@Data
public class LogisticsAccountTransactionEntity extends Auditable {
    @Id
    private UUID id;
    private BigDecimal amount;
    private LogisticsAccountTransactionType transactionType;

    private UUID accountId;
}
