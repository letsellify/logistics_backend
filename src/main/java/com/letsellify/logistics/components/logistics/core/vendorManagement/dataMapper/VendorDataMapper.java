package com.letsellify.logistics.components.logistics.core.vendorManagement.dataMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:54
 */

@Mapper
public interface VendorDataMapper {
    VendorDataMapper INSTANCE = Mappers.getMapper(VendorDataMapper.class);
}
