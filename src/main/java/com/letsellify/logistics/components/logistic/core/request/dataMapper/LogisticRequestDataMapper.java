package com.letsellify.logistics.components.logistic.core.request.dataMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsRequest;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.LogisticItemImageResource;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.LogisticRequestResource;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:00
 */

@Mapper
public interface LogisticRequestDataMapper {
    LogisticRequestDataMapper INSTANCE = Mappers.getMapper(LogisticRequestDataMapper.class);

    LogisticRequestResource dataToResource(LogisticsRequest logisticsRequest);

    LogisticItemImageResource dataToResource(LogisticsItemImage logisticsItemImage);

}
