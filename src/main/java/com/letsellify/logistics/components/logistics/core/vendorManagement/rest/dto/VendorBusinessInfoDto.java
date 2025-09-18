package com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

/**
 * Author: Ahmad Buba
 * Date:8/4/25
 */


public record VendorBusinessInfoDto(
        @JsonProperty("name")
        @NotNull(message = "vendor business name must be present in payload")
        String businessName,
        @JsonProperty("address")
        @NotNull(message = "Vendor office address must be present in payload")
        String businessOfficeAddress,
        @JsonProperty("state")
        @NotNull(message = "Vendor business state must be present in payload")
        String state,
        @JsonProperty("lga")
        @NotNull(message = "Vendor business lg must be present in payload")
        String lg
) {
}
