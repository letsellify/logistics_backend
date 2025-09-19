package com.letsellify.logistics.components.logistics.core.dispatcherManagement.data;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherCurrentlyAcceptingDeliveryResource;

/**
 * Author: Ahmad Buba
 * Date:9/18/25
 */


public record DispatcherCurrentlyAcceptingDelivery(
        boolean currentlyAcceptingDelivery
) {
    public DispatcherCurrentlyAcceptingDeliveryResource getResource() {
        return DispatcherMapper.INSTANCE.toResource(this);
    }
}
