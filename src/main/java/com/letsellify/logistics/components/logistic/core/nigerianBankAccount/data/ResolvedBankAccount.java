package com.letsellify.logistics.components.logistic.core.nigerianBankAccount.data;

import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.dataMapper.BankDataMapper;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.resource.ResolvedBankAccountResource;

/**
 * @author AHMAD BUBA
 * Date:2/28/25
 * Time:21:13
 */

public record ResolvedBankAccount(
  String accountNumber,
  String accountName,
  int bankId
) {

    public ResolvedBankAccountResource getResource() {
        return BankDataMapper.INSTANCE.toResource(this);
    }
}
