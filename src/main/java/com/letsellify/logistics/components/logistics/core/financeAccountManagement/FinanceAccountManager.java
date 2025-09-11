package com.letsellify.logistics.components.logistics.core.financeAccountManagement;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistics.core.agentManagement.AgentManager;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.NoSuchAgentException;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.DispatcherManager;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.NoSuchDispatcherException;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.data.LogisticsAccount;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.data.LogisticsAccountTransactionType;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity.*;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.repository.EscrowedPaymentRepository;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.repository.LogisticsAccountRepository;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.event.AgentTopUpAccountEvent;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.event.DispatcherTopUpAccountEvent;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.event.VendorTopUpAccountEvent;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.exception.FinanceAccountNotFoundException;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.exception.InsufficientFundsException;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.exception.UnableToDetermineAccountException;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.PaystackManager;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.event.ChargeSuccessEvent;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.resource.PaystackInitiateTransactionResponse;
import com.letsellify.logistics.components.logistics.core.vendorManagement.VendorManager;
import com.letsellify.logistics.components.logistics.core.vendorManagement.data.Vendor;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.InCompleteVendorProfileException;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.VendorNotFoundException;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
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
    private final ObjectProvider<VendorManager> vendorManagerProvider;
    private final DispatcherManager dispatcherManager;
    private final AgentManager agentManager;
    private final PaystackManager paystackManager;
    private final SystemFinanceAccountManager systemFinanceAccountManager;



//    @Transactional
//    public void topUpAccount(final UUID userId, final LogisticAppRole userRole, final BigDecimal amount) {
//        log.info("will try to topUp {}", userId);
//        final LogisticsAccountEntity userAccountEntity = this.accountRepository.findByUserId(userId)
//                .orElseGet(() -> LogisticsAccountEntity.getInstance(userId, userRole));
//        log.info("top initiated for account of user {}", userAccountEntity.getUserId());
//        final LogisticsAccountTransactionEntity transactionEntity = userAccountEntity.topUpAccount(amount);
//        this.accountRepository.save(userAccountEntity);
//        // Publish relevant events based on the user's role
//        switch (userAccountEntity.getAppRole()) {
//            case VENDOR:
//                // Currently, VendorTopUpAccountEvent is the only practical use case
//                // as only vendors are the source of money.
//                this.eventPublisher.publishEvent(
//                        new VendorTopUpAccountEvent(
//                                userAccountEntity.getUserId(),
//                                userAccountEntity.getBalance()
//                        )
//                );
//                break;
//            case AGENT:
//                this.eventPublisher.publishEvent(
//                        new AgentTopUpAccountEvent(
//                                userAccountEntity.getUserId(),
//                                userAccountEntity.getBalance()
//                        )
//                );
//                break;
//            case DISPATCHER:
//                this.eventPublisher.publishEvent(
//                        new DispatcherTopUpAccountEvent(
//                                userAccountEntity.getUserId(),
//                                userAccountEntity.getBalance()
//                        )
//                );
//                break;
//            default:
//                // Add log here. this is sensitive
//                break;
//        }
////        return new LogisticsAccountTransaction(transactionEntity);
//    }

    @EventListener
    @Async
    @Transactional
    public void acceptPayment(final ChargeSuccessEvent event) throws FinanceAccountNotFoundException {
        log.info("Charge successful, Webhook recieved from paystack with payment of {}", event.getAmount());
        LogisticsAccountEntity entity = this.accountRepository.findByUserIdAndAppRole(event.getUserId(),event.getUserRole())
                .orElseGet(() -> LogisticsAccountEntity.getInstance(event.getUserId(), event.getUserRole()));
        final BigDecimal amountToTopUp = event.getAmount();
        final BigDecimal currentBalance = entity.getBalance();
        entity.setBalance(currentBalance.add(amountToTopUp));
        this.systemFinanceAccountManager.addPaystackTransaction(event.getAmount(), event.getPaystackPayment(), LogisticsAccountTransactionType.CREDIT);
        this.accountRepository.save(entity);
    }

    /* Only vendor can top up for now. Later we might add switchCase if other actors can top up */
    PaystackInitiateTransactionResponse initializeTopUp(final @NonNull String userName, final LogisticAppRole userRole,  final @NonNull BigDecimal amount) throws VendorNotFoundException, InCompleteVendorProfileException, UserNotFoundException, FinanceAccountNotFoundException {
        if (userRole != LogisticAppRole.VENDOR) {
            throw new FinanceAccountNotFoundException("Only vendors can top up their account");
        }
        final Vendor vendor  = Objects.requireNonNull(this.vendorManagerProvider
                        .getIfAvailable())
                .findAndValidateVendor(userName);
        return this.paystackManager.initializePayment(vendor.getId(), vendor.getEmail(), LogisticAppRole.VENDOR, amount);
    }

    @Transactional
    public void escrowForLogistics(final @NonNull UUID userId, final LogisticAppRole userRole, final String shippingRequestId, final BigDecimal amountForShipping, final BigDecimal amountForStorage) throws FinanceAccountNotFoundException, InsufficientFundsException {
        final LogisticsAccountEntity accountEntity = this.accountRepository.findByUserIdAndAppRole(userId, userRole)
                .orElseThrow(() -> new FinanceAccountNotFoundException("Account not found"));

        final BigDecimal totalSpending = accountEntity.debitForEscrow(amountForShipping, amountForStorage);
        log.info("total spending {}", totalSpending);
        // make account Entity set the reference instead: maning just amount, shipping requestId for the constructor
        // so that once account entity is passed the escrow, it sets the reference, then we just persist accountEntity
        // orphan removal will be useful here. in the case of settling after logistics complete
        final EscrowedPaymentEntity escrowedPaymentEntity = EscrowedPaymentEntity.getInstance(accountEntity, totalSpending, shippingRequestId);
        accountEntity.addEscrowPayment(escrowedPaymentEntity);
        log.info("escrow for {} whose balance is currently {}", accountEntity.getUserId(), accountEntity.getBalance());
        this.accountRepository.save(accountEntity);
        // Publish relevant events based on the user's role
        switch (accountEntity.getAppRole()) {
            case VENDOR:
                // Currently, VendorTopUpAccountEvent is the only practical use case
                // as only vendors are the source of money.
                this.eventPublisher.publishEvent(
                        new VendorTopUpAccountEvent(
                                accountEntity.getUserId(),
                                accountEntity.getBalance()
                        )
                );
                break;
            case AGENT:
                this.eventPublisher.publishEvent(
                        new AgentTopUpAccountEvent(
                                accountEntity.getUserId(),
                                accountEntity.getBalance()
                        )
                );
                break;
            case DISPATCHER:
                this.eventPublisher.publishEvent(
                        new DispatcherTopUpAccountEvent(
                                accountEntity.getUserId(),
                                accountEntity.getBalance()
                        )
                );
                break;
            default:
                // Add log here. this is sensitive
                break;
        }
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


    public BigDecimal getBalance(final @NonNull String userName, final @NonNull LogisticAppRole userRole) throws VendorNotFoundException, NoSuchAgentException, NoSuchDispatcherException, UnableToDetermineAccountException, FinanceAccountNotFoundException {
        UUID userId = null;
        switch (userRole) {
            case VENDOR:
                userId = Objects.requireNonNull(this.vendorManagerProvider.getIfAvailable())
                        .getVendorId(userName);
                break;
            case AGENT:
                userId = this.agentManager.getAgentId(userName);
                break;
            case DISPATCHER:
                userId = this.dispatcherManager.getDispatcherId(userName);
                break;
            default:
                // Add log here. this is sensitive
                break;
        }
        if (userId == null) {
            throw new UnableToDetermineAccountException("User role not visible, hence cant determine account type");
        }
        final LogisticsAccountEntity entity = this.accountRepository.findByUserIdAndAppRole(userId, userRole)
                .orElseThrow(() -> new FinanceAccountNotFoundException("Account not found for user " + userName));
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
