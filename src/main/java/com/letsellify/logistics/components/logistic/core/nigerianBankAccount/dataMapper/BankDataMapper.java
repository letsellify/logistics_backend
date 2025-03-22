package com.letsellify.logistics.components.logistic.core.nigerianBankAccount.dataMapper;

import java.util.HashSet;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.data.Bank;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.data.BankAccount;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.data.Banks;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.data.ResolvedBankAccount;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.resource.BankAccountResource;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.resource.BankResource;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.resource.BankResources;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.resource.ResolvedBankAccountResource;

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
        for (final Bank bank: banks.bankSet()) {
            resources.add(new BankResource(bank.name(), bank.code(), bank.type()));
        }
        return new BankResources(resources);
    }
}
