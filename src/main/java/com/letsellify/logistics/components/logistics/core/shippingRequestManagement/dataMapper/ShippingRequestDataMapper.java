package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.dataMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsRequest;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.rest.resource.LogisticsRequestResource;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:00
 */

@Mapper
public interface ShippingRequestDataMapper {
    ShippingRequestDataMapper INSTANCE = Mappers.getMapper(ShippingRequestDataMapper.class);

    LogisticsRequestResource dataToResource(LogisticsRequest logisticsRequest);

}
