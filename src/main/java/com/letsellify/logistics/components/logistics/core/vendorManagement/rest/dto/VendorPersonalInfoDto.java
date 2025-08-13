package com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto;


import jakarta.validation.constraints.NotBlank;

/**
 * Author: Ahmad Buba
 * Date:8/4/25
 */


public record VendorPersonalInfoDto(
        @NotBlank(message = "Vendor name must be provided")
        String name,
        @NotBlank(message = "Vendor address must be provided")
        String address,
        @NotBlank(message = "Vendor state must be provided")
        String state,
        @NotBlank(message = "Vendor lg must be provided")
        String lg
) {
}
