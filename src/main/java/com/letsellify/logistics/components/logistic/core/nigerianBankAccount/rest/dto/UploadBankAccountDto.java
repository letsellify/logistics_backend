package com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

/**
 * @author AHMAD BUBA
 * Date:3/2/25
 * Time:01:42
 */

public record UploadBankAccountDto(
  @JsonProperty("account_number")
  @NotBlank(message = "Account number cannot be empty.")
  //  @Pattern(regexp = "^\\d{11}$", message = "Account number must be exactly 11 digits.")
  String accountNumber,

  @JsonProperty("bank_code")
  @NotBlank(message = "Bank code cannot be empty.")
  //  @Size(max = 6, message = "Bank code cannot exceed 6 digits.")
  String bankCode
) {}
