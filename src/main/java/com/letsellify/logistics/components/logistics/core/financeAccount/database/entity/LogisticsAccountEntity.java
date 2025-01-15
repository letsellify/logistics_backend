package com.letsellify.logistics.components.logistics.core.financeAccount.database.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.letsellify.logistics.components.logistics.core.auditing.database.entity.Auditable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:22:48
 */

@Entity
@Data
public class LogisticsAccountEntity extends Auditable {
    @Id
    private UUID id;

    private BigDecimal balance;

    private UUID userId;

    @OneToMany
    private List<LogisticsAccountTransactionEntity> transactions;

    protected LogisticsAccountEntity() {
        super();
    }
}
