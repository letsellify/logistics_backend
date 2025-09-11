package com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.data.LogisticsAccountTransactionType;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.exception.InsufficientFundsException;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:22:48
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class LogisticsAccountEntity extends Auditable {
    @Id
    private UUID id;

    private BigDecimal balance;

    private UUID userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LogisticAppRole appRole;

    @OneToMany(mappedBy = "logisticsAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LogisticsAccountTransactionEntity> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EscrowedPaymentEntity> escrowedPayments = new ArrayList<>();

    @Transient
    private final static BigDecimal PROFIT_PERCENT = new BigDecimal(12);

    @Transient
    private final static BigDecimal HUNDRED = new BigDecimal(100);

    protected LogisticsAccountEntity() {
        super();
    }

    public static LogisticsAccountEntity getInstance(final UUID userId, final LogisticAppRole appRole) {
        final LogisticsAccountEntity entity = new LogisticsAccountEntity();
        entity.id = UUID.randomUUID();
        entity.userId = userId;
        entity.appRole = appRole;
        entity.balance = BigDecimal.ZERO;
        return entity;
    }

    public BigDecimal debitForEscrow(final BigDecimal amountForShipping, final BigDecimal amountForStorage) throws InsufficientFundsException {
        final BigDecimal totalSpending = amountForShipping.add(amountForStorage);
        System.out.println("totalSpending = " + totalSpending);
        final BigDecimal tax = totalSpending
                .multiply(PROFIT_PERCENT)
                .divide(HUNDRED, 2, RoundingMode.HALF_UP);
        System.out.println("Amount for shipping is " + amountForShipping);
        System.out.println("Amount for storage is " + amountForStorage);
        System.out.println("totalSpendingAfterTax = " + tax);
        BigDecimal netSpending = totalSpending.add(tax);
        if (this.balance.compareTo(netSpending) < 0) {
            throw new InsufficientFundsException("Insufficient funds, could not escrow this payment");
        }
        this.balance = this.balance.subtract(netSpending);
        return netSpending;
    }

    public void addEscrowPayment(final EscrowedPaymentEntity escrowedPayment) {
        this.escrowedPayments.add(escrowedPayment);
        escrowedPayment.setAccount(this);
    }

//    public LogisticsAccountTransactionEntity topUpAccount(final BigDecimal amount) {
//        final LogisticsAccountTransactionEntity transaction = LogisticsAccountTransactionEntity.getInstance(this, LogisticsAccountTransactionType.CREDIT, this.balance, null);
//        this.transactions.add(transaction);
//        this.balance = this.balance.add(amount);
//        return transaction;
//    }

//    public LogisticsAccountTransactionEntity logisticsTopUpAccount(final BigDecimal amount, final String shippingRequestId) {
//        final LogisticsAccountTransactionEntity transaction = LogisticsAccountTransactionEntity.getInstance(this, LogisticsAccountTransactionType.CREDIT, this.balance, shippingRequestId);
//        this.transactions.add(transaction);
//        this.balance = this.balance.add(amount);
//        return transaction;
//    }

    public void logisticsEscrow(final String shippingRequestId, final BigDecimal amount) throws InsufficientFundsException {
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds, could not escrow this payment");
        }
        final EscrowedPaymentEntity escrowedPayment = EscrowedPaymentEntity.getInstance(this, amount, shippingRequestId);
        this.escrowedPayments.add(escrowedPayment);
        this.balance = this.balance.subtract(amount);
    }

    // tricky, manager first gets escrowedPayment using repository by shippingId, from escrowedPayment gets the account
    // then tells the account settle
    // we could possibly make it get by id then us normal stream method to find it based on id since all transactions have allready been fetched
//    public LogisticsAccountTransactionEntity settle(final EscrowedPaymentEntity escrowedPaymentEntity, final UUID userId, final LogisticAppRole userRole, final BigDecimal amount) {
//        try {
//            escrowedPaymentEntity.settle(userId, userRole, amount);
//        } catch (IllegalStateException e) {
//            throw new RuntimeException(e);
//        }
//        return LogisticsAccountTransactionEntity
//                .getInstance(this, LogisticsAccountTransactionType.DEBIT, amount, escrowedPaymentEntity.getShippingId());
//        // create transaction
//        // check if esc
//    }

}
