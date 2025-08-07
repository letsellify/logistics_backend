package com.letsellify.logistics.components.logistic.core.request.dataMapper;

import com.letsellify.logistics.components.logistic.core.request.data.*;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

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

    VendorLogisticRequestResources dataToVendorResources(LogisticRequests logisticRequests);

    LogisticRequestResources dataToResources(LogisticRequests logisticsRequests);

    /* Custom mapping method to extract the name */
    default String map(ItemCondition condition) {
        return condition.name();
    }

}
