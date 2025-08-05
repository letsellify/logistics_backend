package com.letsellify.logistics.components.logistic.core.vendor.data;


import com.letsellify.logistics.components.logistic.core.vendor.dataMapper.VendorDataMapper;
import com.letsellify.logistics.components.logistic.core.vendor.database.entity.VendorEntity;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorPersonalInformationResource;

/**
 * Author: Ahmad Buba
 * Date:8/5/25
 */


public record VendorPersonalInformation(
        String name,
        String homeAddress,
        String state,
        String lg
) {
    public VendorPersonalInformation(VendorEntity.PersonalInformationEmbeddable personalInformationEmbeddable) {
        this(personalInformationEmbeddable.getName(), personalInformationEmbeddable.getHomeAddress(), personalInformationEmbeddable.getState(), personalInformationEmbeddable.getLg());
    }

    public VendorPersonalInformationResource getResource() {
        return VendorDataMapper.INSTANCE.toResource(this);
    }
}
