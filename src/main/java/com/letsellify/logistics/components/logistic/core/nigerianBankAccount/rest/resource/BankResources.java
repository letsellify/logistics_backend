package com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.resource;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author AHMAD BUBA
 * Date:2/28/25
 * Time:09:54
 */

public record BankResources(
  @JsonProperty("banks")
  Set<BankResource> bankResources,
  @JsonProperty("total_number_of_banks")
  int totalNumberOfBanks
) {
  public BankResources(final Set<BankResource> bankResources) {
    this(bankResources,bankResources.size());
  }
}
