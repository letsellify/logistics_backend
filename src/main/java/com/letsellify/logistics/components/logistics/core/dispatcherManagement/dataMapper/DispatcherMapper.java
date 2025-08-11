package com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.Dispatcher;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.DispatcherInfo;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.LogisticDispatcherInfo;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherPersonalInfoResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.LogisticDispatcherInfoResource;
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

    DispatcherPersonalInfoResource getResource(DispatcherInfo data);

    LogisticDispatcherInfoResource getResource(LogisticDispatcherInfo data);

    DispatcherResource getResource(Dispatcher data);

}
