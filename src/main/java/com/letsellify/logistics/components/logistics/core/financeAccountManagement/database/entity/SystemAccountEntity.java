package com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/26/25
 * Time:09:43
 */


// poplute using db script
@Entity
@EqualsAndHashCode(callSuper = false)
public class SystemAccountEntity extends Auditable {
    @Id
    private String id;

    @Column(nullable = false)
    private BigDecimal balance;

    /**
     * Unidirectional OneToMany - PaystackTransactionEntity will not
     * have a reference back to SystemAccountEntity.
     * JPA will create a join table for this unless you use @JoinColumn.
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "system_account_id", referencedColumnName = "id") // avoids join table
    private List<PaystackTransactionEntity> paystackTransactions = new ArrayList<>();

    // try to make it the other 1 to many that is discouraged by jpa, this is a special case. from transaction
    // they should by no way of getting to system account

    // Prevent cascade operations for critical entities
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
//    private List<LogisticsAccountTransactionEntity> transactions;


//    protected SystemAccountEntity() {
//        super();
//    }

    protected SystemAccountEntity() {}

    public SystemAccountEntity(final String id) {
        super();
        this.id = id;
        this.balance = BigDecimal.ZERO;
    }

//    public void topUp(final LogisticsAccountTransactionEntity transaction) {
//        this.balance = this.balance.add(transaction.getAmount());
//        this.transactions.add(transaction);
//    }

    public void addPaystackTransaction(PaystackTransactionEntity paystackTransactionEntity) {
        this.balance = this.balance.add(paystackTransactionEntity.getAmount());
        this.paystackTransactions.add(paystackTransactionEntity);
    }
}
