package com.letsellify.logistics.components.logistics.core.financeAccountManagement.data;

import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity.LogisticsAccountEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:10:30
 */

@Data
public class LogisticsAccount {
    private UUID id;

    private BigDecimal balance;

    public LogisticsAccount(final LogisticsAccountEntity entity) {
        this.id = entity.getId();
        this.balance = entity.getBalance();
    }
}
