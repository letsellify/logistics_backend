package com.letsellify.logistics.components.logistic.core.dispatcher.dataMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.letsellify.logistics.components.logistic.core.dispatcher.data.DispatcherInfo;
import com.letsellify.logistics.components.logistic.core.dispatcher.data.LogisticDispatcher;
import com.letsellify.logistics.components.logistic.core.dispatcher.data.LogisticDispatcherInfo;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.LogisticDispatcherInfoResource;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.LogisticDispatcherResource;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.DispatcherPersonalInfoResource;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:04
 */

@Mapper
public interface DispatcherMapper {
    DispatcherMapper INSTANCE = Mappers.getMapper(DispatcherMapper.class);

    DispatcherPersonalInfoResource getResource(DispatcherInfo data);

    LogisticDispatcherInfoResource getResource(LogisticDispatcherInfo data);

    LogisticDispatcherResource getResource(LogisticDispatcher data);

}
