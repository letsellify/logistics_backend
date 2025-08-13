package com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto;


import jakarta.validation.constraints.NotNull;

/**
 * Author: Ahmad Buba
 * Date:8/4/25
 */


public record VendorBusinessInfoDto(
        @NotNull(message = "vendor business name must be present in payload")
        String businessName,
        @NotNull(message = "Vendor office address must be present in payload")
        String businessOfficeAddress,
        @NotNull(message = "Vendor business state must be present in payload")
        String state,
        @NotNull(message = "Vendor business lg must be present in payload")
        String lg
) {
}
