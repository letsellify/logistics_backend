package com.letsellify.logistics.components.logistics.core.dispatcherManagement.data;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherPersonalInformationResource;
import com.letsellify.logistics.components.logistics.core.vendorManagement.database.entity.VendorEntity;

/**
 * Author: Ahmad Buba
 * Date:8/14/25
 */


public record DispatcherPersonalInformation(
        String name,
        String state,
        String lga,
        String address
) {

    public DispatcherPersonalInformation(DispatcherEntity.PersonalInfoEmbeddable personalInfoEmbeddable) {
        this(personalInfoEmbeddable.getName(),personalInfoEmbeddable.getState(),personalInfoEmbeddable.getLga(),personalInfoEmbeddable.getAddress());
    }

    public DispatcherPersonalInformationResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }
}
