package com.letsellify.logistics.components.logistics.core.vendorManagement.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: Ahmad Buba
 * Date:8/4/25
 */

public record VendorPersonalInformationResource(String name,
                                                String state, @JsonProperty(value = "lga") String lg, @JsonProperty(value = "address") String homeAddress) {}
