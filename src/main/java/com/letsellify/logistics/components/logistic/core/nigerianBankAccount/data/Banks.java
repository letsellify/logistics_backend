package com.letsellify.logistics.components.logistic.core.nigerianBankAccount.data;

import java.util.Set;

import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.dataMapper.BankDataMapper;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.resource.BankResources;

/**
 * @author AHMAD BUBA
 * Date:2/28/25
 * Time:09:51
 */

public record Banks(
  Set<Bank> bankSet
) {
    public BankResources getResource() {
        return BankDataMapper.INSTANCE.toResource(this);
    }
}
