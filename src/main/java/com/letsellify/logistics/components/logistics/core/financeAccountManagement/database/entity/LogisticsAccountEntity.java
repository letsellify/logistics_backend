package com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import com.letsellify.logistics.common.audit.entity.Auditable;
import com.letsellify.logistics.common.data.LogisticsAppRole;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.data.LogisticsAccountTransactionType;
import com.letsellify.logistics.components.logistics.core.paymentManagement.exception.LogisticsInsufficientFundsException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:22:48
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class LogisticsAccountEntity extends Auditable {
    @Id
    private UUID id;

    private BigDecimal balance;

    @NaturalId
    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private LogisticsAppRole appRole;

    @OneToMany
    private List<LogisticsAccountTransactionEntity> transactions = new ArrayList<>();

    @OneToMany
    private List<EscrowedPaymentEntity> escrowedPayments = new ArrayList<>();

    @Transient
    private final static BigDecimal PROFIT_PERCENT = new BigDecimal(12);

    @Transient
    private final static BigDecimal HUNDRED = new BigDecimal(100);

    protected LogisticsAccountEntity() {
        super();
    }

    public static LogisticsAccountEntity getInstance(final String userEmail, final LogisticsAppRole appRole) {
        final LogisticsAccountEntity entity = new LogisticsAccountEntity();
        entity.id = UUID.randomUUID();
        entity.userEmail = userEmail;
        entity.appRole = appRole;
        entity.balance = BigDecimal.ZERO;
        return entity;
    }

    public BigDecimal debitForEscrow(final BigDecimal amountForShipping, final BigDecimal amountForStorage) throws LogisticsInsufficientFundsException {
        final BigDecimal totalSpending = amountForShipping.add(amountForStorage);
        final BigDecimal totalSpendingAfterTax = totalSpending
                                                   .multiply(PROFIT_PERCENT)
                                                   .divide(HUNDRED, 2, RoundingMode.HALF_UP);
        if (this.balance.compareTo(totalSpendingAfterTax) < 0) {
            throw new LogisticsInsufficientFundsException("Insufficient funds, could not escrow this payment");
        }
        this.balance = this.balance.subtract(totalSpendingAfterTax);
        return totalSpendingAfterTax;
    }

    public void addEscrowPayment(final EscrowedPaymentEntity escrowedPayment) {
        this.escrowedPayments.add(escrowedPayment);
    }

    public LogisticsAccountTransactionEntity topUpAccount(final BigDecimal amount) {
        final LogisticsAccountTransactionEntity transaction = LogisticsAccountTransactionEntity.getInstance(this, LogisticsAccountTransactionType.CREDIT,this.balance,null);
        this.transactions.add(transaction);
        this.balance = this.balance.add(amount);
        return transaction;
    }

    public LogisticsAccountTransactionEntity logisticsTopUpAccount(final BigDecimal amount, final UUID shippingRequestId) {
        final LogisticsAccountTransactionEntity transaction = LogisticsAccountTransactionEntity.getInstance(this, LogisticsAccountTransactionType.CREDIT,this.balance,shippingRequestId);
        this.transactions.add(transaction);
        this.balance = this.balance.add(amount);
        return transaction;
    }

    public void logisticsEscrow(final UUID shippingRequestId, final BigDecimal amount) throws LogisticsInsufficientFundsException {
        if (this.balance.compareTo(amount) < 0) {
            throw new LogisticsInsufficientFundsException("Insufficient funds, could not escrow this payment");
        }
        final EscrowedPaymentEntity escrowedPayment = EscrowedPaymentEntity.getInstance(this, amount, shippingRequestId);
        this.escrowedPayments.add(escrowedPayment);
        this.balance = this.balance.subtract(amount);
    }

    // tricky, manager first gets escrowedPayment using repository by shippingId, from escrowedPayment gets the account
    // then tells the account settle
    // we could possibly make it get by id then us normal stream method to find it based on id since all transactions have allready been fetched
    public LogisticsAccountTransactionEntity settle(final EscrowedPaymentEntity escrowedPaymentEntity,final String userEmail, final LogisticsAppRole userRole, final BigDecimal amount) {
        try {
              escrowedPaymentEntity.settle(userEmail, userRole, amount);
        }
        catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
        return LogisticsAccountTransactionEntity
                 .getInstance(this, LogisticsAccountTransactionType.DEBIT, amount, escrowedPaymentEntity.getShippingId());
        // create transaction
        // check if esc
    }

}
