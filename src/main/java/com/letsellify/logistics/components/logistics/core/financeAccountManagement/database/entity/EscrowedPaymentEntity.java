package com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.data.EscrowStatus;
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
 * Time:07:30
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class EscrowedPaymentEntity extends Auditable {
    @Id
    private UUID id;

    @Column(nullable = false, updatable = false)
    private BigDecimal totalAmount;

    private BigDecimal currentBalance;

    @Column(nullable = false, updatable = false)
    private String shippingId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private LogisticsAccountEntity account;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentSettlementEntity> settlements = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EscrowStatus status;


    protected EscrowedPaymentEntity() {
        super();
    }

    public static EscrowedPaymentEntity getInstance(final LogisticsAccountEntity account, final BigDecimal totalAmount, final String shippingId) {
        final EscrowedPaymentEntity entity = new EscrowedPaymentEntity();
        entity.id = UUID.randomUUID();
        entity.account = account;
        entity.shippingId = shippingId;
        entity.totalAmount = totalAmount;
        entity.status = EscrowStatus.OPEN;
        entity.currentBalance = totalAmount;
        return entity;
    }

    protected void settle(final UUID userId, final LogisticAppRole userRole, final BigDecimal amount) {
        if (this.settlements.size() >= 2) {
            throw new IllegalStateException("Cannot have more than 2 settlements.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Settlement amount must be greater than zero.");
        }

        if (this.currentBalance.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Settlement amount exceeds the current balance.");
        }

        // Add the new settlement
        final PaymentSettlementEntity settlement = PaymentSettlementEntity.getInstance(userId, userRole, amount);
        this.settlements.add(settlement);

        // Update the current balance
        this.currentBalance = this.currentBalance.subtract(amount);

        // check if what we have remaining is the percent profit
        // if so, we set status to settled
        // Update status if fully settled
        if (this.currentBalance.compareTo(BigDecimal.ZERO) == 0) {
            this.status = EscrowStatus.SETTLED;
        }
    }

}
