package com.letsellify.logistics.components.logistics.core.vendorManagement.data;


import com.letsellify.logistics.components.logistics.core.vendorManagement.dataMapper.VendorDataMapper;
import com.letsellify.logistics.components.logistics.core.vendorManagement.database.entity.VendorEntity;
import com.letsellify.logistics.components.logistics.core.vendorManagement.rest.resource.VendorProfileInfoResource;

/**
 * Author: Ahmad Buba
 * Date:8/5/25
 */


public record VendorInfo(
        String email,
        String profilePicture,
        VendorPersonalInformation personalInformation,
        VendorContactInformation contactInformation,
        VendorBusinessInformation businessInformation
) {
    public VendorInfo(String profilePicture, VendorEntity entity) {
        this(entity.getEmail(), profilePicture, new VendorPersonalInformation(entity.getPersonalInformation()), new VendorContactInformation(entity.getContactInformation()), new VendorBusinessInformation(entity.getBusinessInformation()));
    }

    public VendorProfileInfoResource getResource() {
        return VendorDataMapper.INSTANCE.toResource(this);
    }
}
