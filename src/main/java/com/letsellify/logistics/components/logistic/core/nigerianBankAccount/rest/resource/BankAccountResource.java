package com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author AHMAD BUBA
 * Date:3/2/25
 * Time:01:44
 */

public record BankAccountResource(
  @JsonProperty("account_number")
  String accountNumber,
  @JsonProperty("bank_name")
  String bankName
) {}
