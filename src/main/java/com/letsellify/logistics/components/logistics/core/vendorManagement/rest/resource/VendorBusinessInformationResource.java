package com.letsellify.logistics.components.logistics.core.vendorManagement.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: Ahmad Buba
 * Date:8/4/25
 */


public record VendorBusinessInformationResource(
        @JsonProperty(value = "name")
        String businessName,
        @JsonProperty(value = "address")
        String businessOfficeAddress,
        @JsonProperty(value = "state")
        String state,
        @JsonProperty(value = "lga")
        String lg
) {
}
