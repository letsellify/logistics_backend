package com.letsellify.logistics.components.logistics.core.vendorManagement.data;


import com.letsellify.logistics.components.logistics.core.vendorManagement.dataMapper.VendorDataMapper;
import com.letsellify.logistics.components.logistics.core.vendorManagement.database.entity.VendorEntity;
import com.letsellify.logistics.components.logistics.core.vendorManagement.rest.resource.VendorBusinessInformationResource;

/**
 * Author: Ahmad Buba
 * Date:8/5/25
 */


public record VendorBusinessInformation(
        String businessName,
        String businessOfficeAddress,
        String state,
        String lg
) {
    public VendorBusinessInformation(VendorEntity.BusinessInformationEmbeddable businessInformationEmbeddable) {
        this(businessInformationEmbeddable.getBusinessName(), businessInformationEmbeddable.getBusinessOfficeAddress(), businessInformationEmbeddable.getState(), businessInformationEmbeddable.getLg());
    }

    public VendorBusinessInformationResource getResource() {
        return VendorDataMapper.INSTANCE.toResource(this);
    }
}
