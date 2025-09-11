package com.letsellify.logistics.components.logistics.core.financeAccountManagement;


import com.letsellify.logistics.components.logistics.core.financeAccountManagement.config.SystemFinanceAccountProps;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.data.LogisticsAccountTransactionType;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity.PaystackTransactionEntity;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity.SystemAccountEntity;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.repository.SystemAccountRepository;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.exception.FinanceAccountNotFoundException;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.data.PaystackPayment;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Author: Ahmad Buba
 * Date: 8/25/25
 */

@Component
@EnableConfigurationProperties(SystemFinanceAccountProps.class)
@Slf4j
public class SystemFinanceAccountManager {
    private final SystemAccountRepository systemAccountRepository;
    private final SystemFinanceAccountProps systemFinanceAccountProps;
    /* Always returns the singleton instance of the system account loaded/created at application startup. */
    @Getter
    private final SystemAccountEntity adminFinanceAccount; // singleton instance

    public SystemFinanceAccountManager(SystemAccountRepository systemAccountRepository,
                                       SystemFinanceAccountProps systemFinanceAccountProps) {
        this.systemAccountRepository = systemAccountRepository;
        this.systemFinanceAccountProps = systemFinanceAccountProps;
        this.adminFinanceAccount = this.createAdminFinanceAccountIfNotExists();
    }

    /**
     * Ensures there is always a system account with the configured ID.
     * If not found, it is created and persisted once at startup.
     */
    private SystemAccountEntity createAdminFinanceAccountIfNotExists() {
        log.info("System account id {}", systemFinanceAccountProps.id());
        return this.systemAccountRepository
                .findById(systemFinanceAccountProps.id())
                .orElseGet(() -> {
                    log.info("Admin finance account not found. Creating new one with ID={}", systemFinanceAccountProps.id());
                    SystemAccountEntity newAccount = new SystemAccountEntity(systemFinanceAccountProps.id());
                    return systemAccountRepository.save(newAccount);
                });
    }





    /* Adds a transaction and persists the singleton account. */
    @Transactional
    public void addPaystackTransaction(
            @NonNull BigDecimal amount,
            PaystackPayment paystackPayment,
            LogisticsAccountTransactionType transactionType
    ) throws FinanceAccountNotFoundException {
        SystemAccountEntity adminFinanceAccount = systemAccountRepository
                .findById(systemFinanceAccountProps.id())
                .orElseThrow(() -> new FinanceAccountNotFoundException("System account not found!"));
        log.info("About to add {} to system account", amount);


        if (paystackPayment != null) {
            PaystackTransactionEntity paystackTransactionEntity =
                    new PaystackTransactionEntity(paystackPayment, transactionType);
            adminFinanceAccount.addPaystackTransaction(paystackTransactionEntity);
            systemAccountRepository.save(adminFinanceAccount);
        }
    }

}
