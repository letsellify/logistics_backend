package com.letsellify.logistics.components.logistics.core.financeAccountManagement;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.letsellify.logistics.common.data.LogisticsAppRole;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.data.LogisticsAccount;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.data.LogisticsAccountTransaction;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity.EscrowedPaymentEntity;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity.LogisticsAccountEntity;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity.LogisticsAccountTransactionEntity;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.repository.EscrowedPaymentRepository;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.repository.LogisticsAccountRepository;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.event.AgentTopUpAccountEvent;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.event.DispatcherTopUpAccountEvent;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.event.VendorTopUpAccountEvent;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.exception.FinanceAccountNotFoundException;
import com.letsellify.logistics.components.logistics.core.paymentManagement.exception.LogisticsInsufficientFundsException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:22:47
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountManager {
    private final LogisticsAccountRepository accountRepository;
    private final EscrowedPaymentRepository escrowedPaymentRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public LogisticsAccountTransaction topUpAccount(final String userEmail, final LogisticsAppRole userRole, final BigDecimal amount) {
        final LogisticsAccountEntity userAccountEntity = this.accountRepository.findByUserEmail(userEmail)
                                                                                    .orElseGet(() -> LogisticsAccountEntity.getInstance(userEmail, userRole));
        final LogisticsAccountTransactionEntity transactionEntity = userAccountEntity.topUpAccount(amount);
        this.accountRepository.save(userAccountEntity);
        // Publish relevant events based on the user's role
        switch (userAccountEntity.getAppRole()) {
            case VENDOR:
                // Currently, VendorTopUpAccountEvent is the only practical use case
                // as only vendors are the source of money.
                this.eventPublisher.publishEvent(
                  new VendorTopUpAccountEvent(
                    userAccountEntity.getUserEmail(),
                    userAccountEntity.getBalance()
                  )
                );
                break;
            case AGENT:
                this.eventPublisher.publishEvent(
                  new AgentTopUpAccountEvent(
                    userAccountEntity.getUserEmail(),
                    userAccountEntity.getBalance()
                  )
                );
                break;
            case DISPATCHER:
                this.eventPublisher.publishEvent(
                  new DispatcherTopUpAccountEvent(
                    userAccountEntity.getUserEmail(),
                    userAccountEntity.getBalance()
                  )
                );
                break;
            default:
                // Add log here. this is sensitive
                break;
        }
        return new LogisticsAccountTransaction(transactionEntity);
    }

    @Transactional
    public void escrowForLogistics(final @NonNull String userEmail, final LogisticsAppRole userRole, final UUID shippingRequestId, final BigDecimal amountForShipping, final BigDecimal amountForStorage) throws FinanceAccountNotFoundException, LogisticsInsufficientFundsException {
        final LogisticsAccountEntity accountEntity = this.accountRepository.findByUserEmailAndAppRole(userEmail,userRole)
                                                                           .orElseThrow(() -> new FinanceAccountNotFoundException("Account not found"));
        final BigDecimal totalSpending;
        try {
           totalSpending = accountEntity.debitForEscrow(amountForShipping, amountForStorage);
        }
        catch (final LogisticsInsufficientFundsException e) {
            throw new RuntimeException(e);
        }
        // make account Entity set the reference instead: maning just amount, shipping requestId for the constructor
        // so that once account entity is passed the escrow, it sets the reference, then we just persist accountEntity
        // orphan removal will be useful here. in the case of settling after logistics complete
        final EscrowedPaymentEntity escrowedPaymentEntity = EscrowedPaymentEntity.getInstance(accountEntity,totalSpending,shippingRequestId);
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
    public LogisticsAccount chargeAccount(final String email, final BigDecimal amount) {
        final LogisticsAccountEntity userAccountEntity = this.accountRepository.findByUserEmail(email)
                                                                               .orElseThrow();
        final LogisticsAccountEntity modifiedAccountEntity = this.debit(userAccountEntity, amount);
        return new LogisticsAccount(modifiedAccountEntity);

    }

    public void chargeAccountForLogistics(final String email, final BigDecimal amount, final UUID shippingId) {}


    public LogisticsAccount getAccount(final String username) {
        final LogisticsAccountEntity entity = this.accountRepository.findByUserEmail(username)
                                                                    .orElseThrow();
        return new LogisticsAccount(entity);
    }



    public BigDecimal getBalance(final String email) {
        final LogisticsAccountEntity entity = this.accountRepository.findByUserEmail(email)
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
