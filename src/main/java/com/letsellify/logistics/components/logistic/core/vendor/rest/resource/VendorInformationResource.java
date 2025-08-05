package com.letsellify.logistics.components.logistic.core.vendor.rest.resource;


/**
 * Author: Ahmad Buba
 * Date:8/4/25
 */
 
 
public record VendorInformationResource(
        String profilePicture,
        VendorPersonalInformationResource personalInformation,
        VendorContactInformationResource contactInformation,
        VendorBusinessInformationResource businessInformation
) {
}
