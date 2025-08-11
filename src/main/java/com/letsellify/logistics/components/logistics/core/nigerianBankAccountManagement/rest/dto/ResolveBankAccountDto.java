package com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * @author AHMAD BUBA
 * Date:2/28/25
 * Time:21:20
 */

public record ResolveBankAccountDto(
        @JsonProperty("account_number")
        @NotBlank(message = "Account number cannot be empty.")
//  @Pattern(regexp = "^\\d{11}$", message = "Account number must be exactly 11 digits.")
        String accountNumber,

        @JsonProperty("bank_code")
        @NotBlank(message = "Bank code cannot be empty.")
//  @Size(max = 6, message = "Bank code cannot exceed 6 digits.")
        String bankCode
) {
}
