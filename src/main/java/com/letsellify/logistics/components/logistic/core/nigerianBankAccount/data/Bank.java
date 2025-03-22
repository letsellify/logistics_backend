package com.letsellify.logistics.components.logistic.core.nigerianBankAccount.data;

import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.dataMapper.BankDataMapper;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.database.entity.BankEntity;
import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.rest.resource.BankResource;

/**
 * @author AHMAD BUBA
 * Date:2/28/25
 * Time:09:19
 */

public record Bank(
  String name,
  String code,
  String type
) {
    public Bank(final BankEntity entity) {
        this(entity.getName(), entity.getCode(), entity.getType());
    }

    public BankResource getResource() {
        return BankDataMapper.INSTANCE.toResource(this);
    }
}
