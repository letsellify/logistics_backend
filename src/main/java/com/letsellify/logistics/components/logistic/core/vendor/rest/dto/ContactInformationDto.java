package com.letsellify.logistics.components.logistic.core.vendor.rest.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Author: Ahmad Buba
 * Date:8/4/25
 */


public record ContactInformationDto(
        @NotBlank
        @Size(min = 11, max = 14)
        String phoneNumber,
        @NotNull
        String whatsAppPhoneNumber
) {
}
