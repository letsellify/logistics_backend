package com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Author: Ahmad Buba
 * Date:8/4/25
 */


public record VendorContactInfoDto(
        @NotBlank
        @Size(min = 11, max = 14, message = "Invalid phonenumber length")
        String phoneNumber,
        @NotNull(message = "Vendor whatsapp number must be present in payload")
        String whatsAppPhoneNumber
) {
}
