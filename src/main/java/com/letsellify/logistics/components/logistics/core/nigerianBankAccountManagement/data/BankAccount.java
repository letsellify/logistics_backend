package com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.data;

import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.dataMapper.BankDataMapper;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.database.entity.BankAccountEntity;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.resource.BankAccountResource;

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
