package com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.data;

import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.dataMapper.BankDataMapper;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.resource.ResolvedBankAccountResource;

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
