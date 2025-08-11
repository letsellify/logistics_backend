package com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.data;

import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.dataMapper.BankDataMapper;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.resource.BankResources;

import java.util.Set;

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
