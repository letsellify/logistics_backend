package com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto;


import jakarta.validation.constraints.NotNull;

/**
 * Author: Ahmad Buba
 * Date:8/13/25
 */


public record VendorProfileInfoDto(
        @NotNull(message = "Vendor personal information must be provided")
        VendorPersonalInfoDto personalInformation,
        @NotNull(message = "Vendor contact information must be provided")
        VendorContactInfoDto contactInformation,
        VendorBusinessInfoDto businessInformation
) {
}
