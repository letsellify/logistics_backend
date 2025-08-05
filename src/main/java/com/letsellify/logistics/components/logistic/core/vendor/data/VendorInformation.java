package com.letsellify.logistics.components.logistic.core.vendor.data;


import com.letsellify.logistics.components.logistic.core.vendor.dataMapper.VendorDataMapper;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorInformationResource;

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
