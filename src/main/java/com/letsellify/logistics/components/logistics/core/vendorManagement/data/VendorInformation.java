package com.letsellify.logistics.components.logistics.core.vendorManagement.data;


import com.letsellify.logistics.components.logistics.core.vendorManagement.dataMapper.VendorDataMapper;
import com.letsellify.logistics.components.logistics.core.vendorManagement.rest.resource.VendorInformationResource;

/**
 * Author: Ahmad Buba
 * Date:8/5/25
 */


public record VendorInformation(
        String profilePicture,
        VendorPersonalInformation personalInformation,
        VendorContactInformation contactInformation,
        VendorBusinessInformation businessInformation
) {
    public VendorInformationResource getResource() {
        return VendorDataMapper.INSTANCE.toResource(this);
    }
}
