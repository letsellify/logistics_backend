package com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:04
 */

@Mapper
public interface DispatcherMapper {
    DispatcherMapper INSTANCE = Mappers.getMapper(DispatcherMapper.class);
}
