package com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.Dispatcher;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.DispatcherInfo;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.DispatchersInfo;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.LogisticDispatcherInfo;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.LogisticDispatcherInfoResource;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.DispatcherProfileInfoResources;
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

    DispatcherProfileInfoResource getResource(DispatcherInfo data);

    LogisticDispatcherInfoResource getResource(LogisticDispatcherInfo data);

    DispatcherResource getResource(Dispatcher data);

    DispatcherProfileInfoResources getResources(DispatchersInfo data);

}
