package com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.letsellify.logistics.common.audit.entity.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:1/26/25
 * Time:09:43
 */


// poplute using db script
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemAccountEntity extends Auditable {
    @Id
    private UUID id;

    @Column(nullable = false)
    private BigDecimal balance;

    // try to make it the other 1 to many that is discouraged by jpa, this is a special case. from transaction
    // they should by no way of getting to system account

    // Prevent cascade operations for critical entities
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
    private List<LogisticsAccountTransactionEntity> transactions;


    protected SystemAccountEntity() {
        super();
    }

    public void topUp(final LogisticsAccountTransactionEntity transaction) {
        this.balance = this.balance.add(transaction.getAmount());
        this.transactions.add(transaction);
    }
}
