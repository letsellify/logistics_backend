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
        this(
                entity.getEmail(),
                profilePicture,
                entity.getPersonalInformation() != null ? new VendorPersonalInformation(entity.getPersonalInformation()) : null,
                entity.getContactInformation() != null ? new VendorContactInformation(entity.getContactInformation()) : null,
                entity.getBusinessInformation() != null ? new VendorBusinessInformation(entity.getBusinessInformation()) : null
        );
    }

    public VendorProfileInfoResource getResource() {
        return VendorDataMapper.INSTANCE.toResource(this);
    }
}
