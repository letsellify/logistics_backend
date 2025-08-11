package com.letsellify.logistics.components.logistics.core.vendorManagement.rest.resource;


/**
 * Author: Ahmad Buba
 * Date:8/4/25
 */


public record VendorBusinessInformationResource(
        String businessName,
        String businessOfficeAddress,
        String state,
        String lg
) {
}
