package com.letsellify.logistics.components.logistics.core.vendorManagement.rest.resource;


/**
 * Author: Ahmad Buba
 * Date:8/4/25
 */


public record VendorProfileInfoResource(
        String email,
        String profilePicture,
        VendorPersonalInformationResource personalInformation,
        VendorContactInformationResource contactInformation,
        VendorBusinessInformationResource businessInformation
) {
}
