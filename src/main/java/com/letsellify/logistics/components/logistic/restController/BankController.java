package com.letsellify.logistics.components.logistic.restController;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.BankAccountDataService;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.dto.ResolveBankAccountDto;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.dto.UploadBankAccountDto;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.resource.BankAccountResource;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.resource.BankResources;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.resource.ResolvedBankAccountResource;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:2/24/25
 * Time:16:04
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bank")
@Tag(name = "Banking API", description = "API's for operation relating to banks")
public class BankController {
    private final BankAccountDataService bankDataService;

    @GetMapping("/all")
    public BankResources getAllBanks() {
        return this.bankDataService.getBanks();
    }

    @GetMapping("/resolve-account")
    public ResolvedBankAccountResource resolveAccountNumber(@Valid @RequestBody final ResolveBankAccountDto resolveBankAccountDto) {
        return this.bankDataService.resolveAccountNumber(resolveBankAccountDto);
    }

    @PostMapping("/upload-account")
    public BankAccountResource uploadBankAccount(final @NonNull Authentication authentication, @Valid @RequestBody final UploadBankAccountDto uploadBankAccountDto) {
        return this.bankDataService.uploadBankAccount(authentication, uploadBankAccountDto);
    }

    @PatchMapping("/update-account")
    public BankAccountResource updateBankAccount(final @NonNull Authentication authentication, @Valid @RequestBody final UploadBankAccountDto uploadBankAccountDto) {
        return this.bankDataService.updateBankAccount(authentication,uploadBankAccountDto);
    }
}
