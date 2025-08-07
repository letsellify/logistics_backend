package com.letsellify.logistics.components.logistic.core.financeAccount;


import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.common.restException.LogisticsRestException;
import com.letsellify.logistics.common.security.data.LogisticsAppSecurityUser;
import com.letsellify.logistics.components.logistic.core.agent.exception.NoSuchAgentException;
import com.letsellify.logistics.components.logistic.core.dispatcher.exception.NoSuchDispatcherException;
import com.letsellify.logistics.components.logistic.core.financeAccount.exception.FinanceAccountNotFoundException;
import com.letsellify.logistics.components.logistic.core.financeAccount.exception.UnableToDetermineAccountException;
import com.letsellify.logistics.components.logistic.core.vendor.exception.VendorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Author: Ahmad Buba
 * Date: 8/7/25
 */

@Service
@RequiredArgsConstructor
public class FinanceAccountDataService {
    private final FinanceAccountManager financeAccountManager;

    public BigDecimal getFinanceAccountBalance(final Authentication authentication) {
        LogisticsAppSecurityUser securityUser = (LogisticsAppSecurityUser) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.isEmpty()) {
            throw new LogisticsRestException(HttpStatus.FORBIDDEN, "No role found for user");
        }

        String roleClaim = authorities.iterator().next().getAuthority(); // e.g., "ROLE_VENDOR"
        String enumName = roleClaim.replace("ROLE_", "");

        LogisticAppRole role;
        try {
            role = LogisticAppRole.valueOf(enumName);
        } catch (IllegalArgumentException e) {
            throw new LogisticsRestException(HttpStatus.FORBIDDEN, "Invalid role: " + roleClaim);
        }
        try {
            return this.financeAccountManager.getBalance(authentication.getName(), role);
        } catch (VendorNotFoundException | NoSuchAgentException | NoSuchDispatcherException |
                 UnableToDetermineAccountException | FinanceAccountNotFoundException e) {
            throw new LogisticsRestException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
