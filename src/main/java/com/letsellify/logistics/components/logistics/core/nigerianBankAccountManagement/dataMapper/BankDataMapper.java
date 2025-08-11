package com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.dataMapper;

import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.data.Bank;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.data.BankAccount;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.data.Banks;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.data.ResolvedBankAccount;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.resource.BankAccountResource;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.resource.BankResource;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.resource.BankResources;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.resource.ResolvedBankAccountResource;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;

/**
 * @author AHMAD BUBA
 * Date:2/28/25
 * Time:20:39
 */

@Mapper
public interface BankDataMapper {
    BankDataMapper INSTANCE = Mappers.getMapper(BankDataMapper.class);

    BankResource toResource(Bank data);

    Set<BankResource> toResource(Set<Bank> data);

    ResolvedBankAccountResource toResource(ResolvedBankAccount data);

    BankAccountResource toResource(BankAccount data);

    default BankResources toResource(final Banks banks) {
        final Set<BankResource> resources = new HashSet<>();
        for (final Bank bank : banks.bankSet()) {
            resources.add(new BankResource(bank.name(), bank.code(), bank.type()));
        }
        return new BankResources(resources);
    }
}
