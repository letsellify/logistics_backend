package com.letsellify.logistics.components.logistics.restController;


import com.letsellify.logistics.components.logistics.core.financeAccountManagement.FinanceAccountDataService;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.rest.dto.TopUpAccountDto;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.resource.PaystackInitiateTransactionResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Author: Ahmad Buba
 * Date: 8/7/25
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/finance/")
@Tag(name = "Finance API", description = "API's for managing finances")
public class FinanceAccountController {
    private final FinanceAccountDataService financeAccountDataService;

    @GetMapping("account")
    public BigDecimal getFinanceAccountBalance(final Authentication authentication) {
        return this.financeAccountDataService.getFinanceAccountBalance(authentication);
    }

    @PostMapping("account")
    public PaystackInitiateTransactionResponse initializeTransaction(final Authentication authentication, final @NonNull TopUpAccountDto topUpAccountDto) {
        return this.financeAccountDataService.initializeTopUp(authentication,topUpAccountDto);
    }
}
