package com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.exception.BankAccountExistException;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.exception.NoSuchBankException;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.exception.NoUserBankAccountException;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.dto.ResolveBankAccountDto;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.dto.UploadBankAccountDto;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.resource.BankAccountResource;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.resource.BankResources;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.resource.ResolvedBankAccountResource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @author AHMAD BUBA
 * Date:2/28/25
 * Time:20:30
 */

@Service
@RequiredArgsConstructor
public class BankAccountDataService {
    private final BankAccountManager bankAccountManager;

    public BankResources getBanks() {
        return this.bankAccountManager.getAllBanks()
                .getResource();
    }

    public ResolvedBankAccountResource resolveAccountNumber(final @NonNull ResolveBankAccountDto resolveBankAccountDto) {
        return this.bankAccountManager.resolveAccountNumber(resolveBankAccountDto.accountNumber(), resolveBankAccountDto.bankCode())
                .getResource();
    }

    public BankAccountResource uploadBankAccount(final @NonNull Authentication authentication, final @NonNull UploadBankAccountDto uploadBankAccountDto) {
        try {
            return this.bankAccountManager.uploadbankAccount(authentication.getName(), uploadBankAccountDto.accountNumber(), uploadBankAccountDto.bankCode())
                    .getResource();
        } catch (final NoSuchBankException | BankAccountExistException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public BankAccountResource updateBankAccount(final @NonNull Authentication authentication, final @NonNull UploadBankAccountDto uploadBankAccountDto) {
        try {
            return this.bankAccountManager.updateBankAccount(authentication.getName(), uploadBankAccountDto.accountNumber(), uploadBankAccountDto.bankCode())
                    .getResource();
        } catch (NoSuchBankException | NoUserBankAccountException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }
}
