package com.letsellify.logistics.components.logistics.core.dispatcherManagement.data;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherBusinessInformationResource;

/**
 * Author: Ahmad Buba
 * Date:8/14/25
 */


public record DispatcherBusinessInformation(
        String name,
        String address,
        String state,
        String lga
) {
    public DispatcherBusinessInformation(DispatcherEntity.DispatchDetailEmbeddable dispatchDetailEmbeddable) {
        this(dispatchDetailEmbeddable.getBusinessName(), dispatchDetailEmbeddable.getBusinessOfficeAddress(),dispatchDetailEmbeddable.getBusinessState(), dispatchDetailEmbeddable.getBusinessLga());
    }

    public DispatcherBusinessInformationResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }
}
