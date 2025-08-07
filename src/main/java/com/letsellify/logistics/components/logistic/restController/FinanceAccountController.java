package com.letsellify.logistics.components.logistic.restController;


import com.letsellify.logistics.components.logistic.core.financeAccount.FinanceAccountDataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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
}
