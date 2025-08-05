package com.letsellify.logistics.components.logistic.core.financeAccount;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistic.core.financeAccount.data.LogisticsAccount;
import com.letsellify.logistics.components.logistic.core.financeAccount.database.entity.EscrowedPaymentEntity;
import com.letsellify.logistics.components.logistic.core.financeAccount.database.entity.LogisticsAccountEntity;
import com.letsellify.logistics.components.logistic.core.financeAccount.database.entity.LogisticsAccountTransactionEntity;
import com.letsellify.logistics.components.logistic.core.financeAccount.database.repository.EscrowedPaymentRepository;
import com.letsellify.logistics.components.logistic.core.financeAccount.database.repository.LogisticsAccountRepository;
import com.letsellify.logistics.components.logistic.core.financeAccount.event.AgentTopUpAccountEvent;
import com.letsellify.logistics.components.logistic.core.financeAccount.event.DispatcherTopUpAccountEvent;
import com.letsellify.logistics.components.logistic.core.financeAccount.event.VendorTopUpAccountEvent;
import com.letsellify.logistics.components.logistic.core.financeAccount.exception.FinanceAccountNotFoundException;
import com.letsellify.logistics.components.logistic.core.financeAccount.exception.InsufficientFundsException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:22:47
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class FinanceAccountManager {
    private final LogisticsAccountRepository accountRepository;
    private final EscrowedPaymentRepository escrowedPaymentRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public void topUpAccount(final UUID userId, final LogisticAppRole userRole, final BigDecimal amount) {
        log.info("will try to topUp {}", userId);
        final LogisticsAccountEntity userAccountEntity = this.accountRepository.findByUserId(userId)
                .orElseGet(() -> LogisticsAccountEntity.getInstance(userId, userRole));
        log.info("top initiated for account of user {}", userAccountEntity.getUserId());
        final LogisticsAccountTransactionEntity transactionEntity = userAccountEntity.topUpAccount(amount);
        this.accountRepository.save(userAccountEntity);
        // Publish relevant events based on the user's role
        switch (userAccountEntity.getAppRole()) {
            case VENDOR:
                // Currently, VendorTopUpAccountEvent is the only practical use case
                // as only vendors are the source of money.
                this.eventPublisher.publishEvent(
                        new VendorTopUpAccountEvent(
                                userAccountEntity.getUserId(),
                                userAccountEntity.getBalance()
                        )
                );
                break;
            case AGENT:
                this.eventPublisher.publishEvent(
                        new AgentTopUpAccountEvent(
                                userAccountEntity.getUserId(),
                                userAccountEntity.getBalance()
                        )
                );
                break;
            case DISPATCHER:
                this.eventPublisher.publishEvent(
                        new DispatcherTopUpAccountEvent(
                                userAccountEntity.getUserId(),
                                userAccountEntity.getBalance()
                        )
                );
                break;
            default:
                // Add log here. this is sensitive
                break;
        }
//        return new LogisticsAccountTransaction(transactionEntity);
    }

    @Transactional
    public void escrowForLogistics(final @NonNull UUID userId, final LogisticAppRole userRole, final String shippingRequestId, final BigDecimal amountForShipping, final BigDecimal amountForStorage) throws FinanceAccountNotFoundException, InsufficientFundsException {
        final LogisticsAccountEntity accountEntity = this.accountRepository.findByUserIdAndAppRole(userId, userRole)
                .orElseThrow(() -> new FinanceAccountNotFoundException("Account not found"));

        BigDecimal totalSpending = accountEntity.debitForEscrow(amountForShipping, amountForStorage);
        // make account Entity set the reference instead: maning just amount, shipping requestId for the constructor
        // so that once account entity is passed the escrow, it sets the reference, then we just persist accountEntity
        // orphan removal will be useful here. in the case of settling after logistics complete
        final EscrowedPaymentEntity escrowedPaymentEntity = EscrowedPaymentEntity.getInstance(accountEntity, totalSpending, shippingRequestId);
        accountEntity.addEscrowPayment(escrowedPaymentEntity);
        this.accountRepository.save(accountEntity);
    }

//    @Transactional
//    public void settle(final BigDecimal amount, final UUID shippingRequestId) throws InvalidLogisticTransactionException {
//        final EscrowedPaymentEntity escrowedPaymentEntity = this.escrowedPaymentRepository.findByShippingId(shippingRequestId).orElseThrow(() -> new InvalidLogisticTransactionException("No such transaction, Invalid requestId"));
//        final LogisticsAccountEntity account = escrowedPaymentEntity.getAccount();
//        try {
//            LogisticsAccountTransactionEntity transactionEntity = account.settle(escrowedPaymentEntity,account.getUserEmail(),account.getAppRole(),amount);
//            this.accountRepository.save(account);
//
//
//            // save transaction Entity
//            // pass it system account so it tops up
//            // we could return a data(LogisticsTransaction)
//        }
//    }


    @Transactional
    public LogisticsAccount chargeAccount(final UUID userId, final LogisticAppRole userRole, BigDecimal amount) {
        final LogisticsAccountEntity userAccountEntity = this.accountRepository.findByUserIdAndAppRole(userId, userRole)
                .orElseThrow();
        final LogisticsAccountEntity modifiedAccountEntity = this.debit(userAccountEntity, amount);
        return new LogisticsAccount(modifiedAccountEntity);

    }

    public void chargeAccountForLogistics(final String email, final BigDecimal amount, final UUID shippingId) {
    }


    public LogisticsAccount getAccount(final UUID userId, final LogisticAppRole userRole) {
        final LogisticsAccountEntity entity = this.accountRepository.findByUserIdAndAppRole(userId, userRole)
                .orElseThrow();
        return new LogisticsAccount(entity);
    }


    public BigDecimal getBalance(final UUID userId, final LogisticAppRole userRole) {
        final LogisticsAccountEntity entity = this.accountRepository.findByUserIdAndAppRole(userId, userRole)
                .orElseThrow();
        return entity.getBalance();
    }


    private LogisticsAccountEntity credit(final LogisticsAccountEntity entity, final BigDecimal amount) {
        entity.setBalance(entity.getBalance().add(amount));
        this.accountRepository.save(entity);
        return entity;
    }

    private LogisticsAccountEntity debit(final LogisticsAccountEntity entity, final BigDecimal amount) {
        entity.setBalance(entity.getBalance().subtract(amount));
        this.accountRepository.save(entity);
        return entity;
    }

}
