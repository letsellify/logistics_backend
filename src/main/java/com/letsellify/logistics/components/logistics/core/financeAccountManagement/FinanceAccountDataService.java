package com.letsellify.logistics.components.logistics.core.financeAccountManagement;


import com.letsellify.logistics.common.restException.LogisticsRestException;
import com.letsellify.logistics.common.util.CookieHandler;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.NoSuchAgentException;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.NoSuchDispatcherException;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.exception.FinanceAccountNotFoundException;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.exception.UnableToDetermineAccountException;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.rest.dto.TopUpAccountDto;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.resource.PaystackInitiateTransactionResponse;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.InCompleteVendorProfileException;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.VendorNotFoundException;
import com.letsellify.logistics.common.util.SecurityUserRoleExtractorHandler;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Author: Ahmad Buba
 * Date: 8/7/25
 */

@Service
@RequiredArgsConstructor
public class FinanceAccountDataService {
    private final FinanceAccountManager financeAccountManager;

    public BigDecimal getFinanceAccountBalance(final Authentication authentication) {
        try {
            return this.financeAccountManager.getBalance(authentication.getName(), SecurityUserRoleExtractorHandler.extractUserRole(authentication));
        } catch (VendorNotFoundException | NoSuchAgentException | NoSuchDispatcherException |
                 UnableToDetermineAccountException | FinanceAccountNotFoundException e) {
            throw new LogisticsRestException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    public PaystackInitiateTransactionResponse initializeTopUp(final Authentication authentication, final @NonNull TopUpAccountDto topUpAccountDto) {
        try {
            return this.financeAccountManager.initializeTopUp(authentication.getName(), SecurityUserRoleExtractorHandler.extractUserRole(authentication), topUpAccountDto.amount());
        } catch (UserNotFoundException | VendorNotFoundException e) {
            throw new LogisticsRestException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InCompleteVendorProfileException e) {
            throw new LogisticsRestException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (FinanceAccountNotFoundException e) {
            throw new LogisticsRestException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }
}
