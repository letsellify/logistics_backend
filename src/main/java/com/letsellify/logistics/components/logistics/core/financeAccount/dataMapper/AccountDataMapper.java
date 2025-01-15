package com.letsellify.logistics.components.logistics.core.financeAccount.dataMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author AHMAD BUBA
 * Date:1/13/25
 * Time:13:22
 */

@Mapper
public interface AccountDataMapper {
    AccountDataMapper INSTANCE = Mappers.getMapper(AccountDataMapper.class);
}
