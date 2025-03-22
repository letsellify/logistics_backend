package com.letsellify.logistics.components.logistic.core.nigerianBankAccount.data;

import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.dataMapper.BankDataMapper;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.database.entity.BankAccountEntity;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.resource.BankAccountResource;

/**
 * @author AHMAD BUBA
 * Date:3/1/25
 * Time:12:25
 */

public record BankAccount(
  String accountNumber,
  String bankName,
  String bankCode
) {
    public BankAccount(final BankAccountEntity entity) {
        this(
          entity.getAccountNumber(),
          entity.getBank().getName(),
          entity.getBank().getCode()
        );
    }

    public BankAccountResource getResource() {
        return BankDataMapper.INSTANCE.toResource(this);
    }
}
