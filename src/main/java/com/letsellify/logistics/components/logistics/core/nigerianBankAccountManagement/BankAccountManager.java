package com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement;

import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.data.Bank;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.data.BankAccount;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.data.Banks;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.data.ResolvedBankAccount;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.database.entity.BankAccountEntity;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.database.entity.BankEntity;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.database.repository.BankAccountRepository;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.database.repository.BankRepository;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.exception.BankAccountExistException;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.exception.NoSuchBankException;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.exception.NoUserBankAccountException;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.PaystackManager;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.resource.PaystackListBanksResponse;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.resource.PaystackResolveBankAccountResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author AHMAD BUBA
 * Date:2/28/25
 * Time:20:30
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class BankAccountManager implements CommandLineRunner {
    private final BankRepository bankRepository;
    private final BankAccountRepository bankAccountRepository;
    private final PaystackManager paystackManager;

    @Override
    public void run(final String... args) throws Exception {
        if (this.bankRepository.count() <= 0) {
            final Set<BankEntity> banks = new HashSet<>();
            final PaystackListBanksResponse paystackListBanksResponse = this.paystackManager.getBanks();
            for (final PaystackListBanksResponse.Bank paystackBankResponse : paystackListBanksResponse.getData()) {
                final BankEntity bankEntity = BankEntity.builder()
                        .id(paystackBankResponse.getId())
                        .name(paystackBankResponse.getName())
                        .slug(paystackBankResponse.getSlug())
                        .code(paystackBankResponse.getCode())
                        .longcode(paystackBankResponse.getLongcode())
                        .gateway(paystackBankResponse.getGateway())
                        .payWithBank(paystackBankResponse.isPayWithBank())
                        .active(paystackBankResponse.isActive())
                        .isDeleted(paystackBankResponse.isDeleted())
                        .country(paystackBankResponse.getCountry())
                        .currency(paystackBankResponse.getCurrency())
                        .type(paystackBankResponse.getType())
                        .createdAt(paystackBankResponse.getCreatedAt())
                        .updatedAt(paystackBankResponse.getUpdatedAt())
                        .build();
                banks.add(bankEntity);
            }
            this.bankRepository.saveAll(banks);
        }
    }

    @Transactional
    BankAccount uploadbankAccount(final @NonNull String username, final @NonNull String accountNumber, final @NonNull String bankCode) throws NoSuchBankException, BankAccountExistException {
        // Check if null, empty, or contains only whitespace
        if (accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty or whitespace only.");
        }

        // Check if it contains exactly 11 digits
        if (!accountNumber.matches("\\d{11}")) {
            throw new IllegalArgumentException("Phone number must be exactly 11 digits.");
        }

        final BankEntity bankEntity = this.bankRepository.findByCode(bankCode)
                .orElseThrow(() -> new NoSuchBankException("Bank code provided is wrong and corresponds to no bank in Nigeria"));
        if (this.bankAccountRepository.existsByUsername(username)) {
            throw new BankAccountExistException("You have previously uploaded an account, try updating it rather");
        }
        final BankAccountEntity bankAccountEntity = new BankAccountEntity(username, accountNumber, bankEntity);
        this.bankAccountRepository.save(bankAccountEntity);
        return new BankAccount(bankAccountEntity);
    }

    @Transactional
    BankAccount updateBankAccount(final @NonNull String username, final @NonNull String accountNumber, final @NonNull String bankCode) throws NoSuchBankException, NoUserBankAccountException {
        // Check if null, empty, or contains only whitespace
        if (accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty or whitespace only.");
        }

        // Check if it contains exactly 11 digits
        if (!accountNumber.matches("\\d{11}")) {
            throw new IllegalArgumentException("Phone number must be exactly 11 digits.");
        }

        final BankEntity bankEntity = this.bankRepository.findByCode(bankCode)
                .orElseThrow(() -> new NoSuchBankException("Bank code provided is wrong and corresponds to no bank in Nigeria"));
        final BankAccountEntity bankAccountEntity = this.bankAccountRepository.findByUsername(username)
                .orElseThrow(() -> new NoUserBankAccountException("User has no bank account, he should create 1 rather than update"));
        bankAccountEntity.update(accountNumber, bankEntity);
        this.bankAccountRepository.save(bankAccountEntity);
        return new BankAccount(bankAccountEntity);
    }

    @Transactional(readOnly = true)
    Banks getAllBanks() {
        final Set<Bank> banks = new HashSet<>();
        final List<BankEntity> bankEntityList = this.bankRepository.findAll();
        for (final BankEntity bankEntity : bankEntityList) {
            banks.add(new Bank(bankEntity));
        }
        return new Banks(banks);
    }

    ResolvedBankAccount resolveAccountNumber(final String accountNumber, final String bankCode) {
        final PaystackResolveBankAccountResponse paystackResolveBankAccountResponse = this.paystackManager
                .resolveAccountNumber(accountNumber, bankCode);
        return new ResolvedBankAccount(
                paystackResolveBankAccountResponse.getData().getAccountNumber(),
                paystackResolveBankAccountResponse.getData().getAccountName(),
                paystackResolveBankAccountResponse.getData().getBankId()
        );
    }
}
