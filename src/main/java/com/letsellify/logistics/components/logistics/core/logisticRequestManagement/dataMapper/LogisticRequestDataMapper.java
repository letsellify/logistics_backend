package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.dataMapper;

import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.*;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.*;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:00
 */

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LogisticRequestDataMapper {
    LogisticRequestDataMapper INSTANCE = Mappers.getMapper(LogisticRequestDataMapper.class);

    FullLogisticRequestResource dataToResource(LogisticRequest logisticsRequest);

    LogisticRequestResource dataToVendorResource(LogisticRequest logisticsRequest);

    LogisticItemImageResource dataToResource(LogisticsItemImage logisticsItemImage);

    LogisticItemResource dataToResource(Item item);

    LogisticRequestResources dataToVendorResources(LogisticRequests logisticRequests);

    LogisticRequestResources dataToResources(LogisticRequests logisticsRequests);

    LogisticDispatcherResource dataToResource(LogisticDispatcher logisticDispatcher);

    LogisticAgentResource dataToResource(LogisticAgent data);

    LogisticVendorResource dataToResource(Sender data);


    /* Custom mapping method to extract the name */
    default String map(ItemCondition condition) {
        return condition.name();
    }

}
