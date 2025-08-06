package com.letsellify.logistics.components.logistic.core.request.dataMapper;

import com.letsellify.logistics.components.logistic.core.request.data.Item;
import com.letsellify.logistics.components.logistic.core.request.data.ItemCondition;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.LogisticItemResource;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.VendorLogisticRequestResource;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticRequest;
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

    LogisticRequestResource dataToResource(LogisticRequest logisticsRequest);

    VendorLogisticRequestResource dataToVendorResource(LogisticRequest logisticsRequest);

    LogisticItemImageResource dataToResource(LogisticsItemImage logisticsItemImage);

    LogisticItemResource dataToResource(Item item);

    /* Custom mapping method to extract the name */
    default String map(ItemCondition condition) {
        return condition.name();
    }

}
