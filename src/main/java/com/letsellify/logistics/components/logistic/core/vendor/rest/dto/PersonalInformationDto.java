package com.letsellify.logistics.components.logistic.core.vendor.rest.dto;


import jakarta.validation.constraints.NotBlank;

/**
 * Author: Ahmad Buba
 * Date:8/4/25
 */


public record PersonalInformationDto(
        @NotBlank
        String name,
        @NotBlank
        String homeAddress,
        @NotBlank
        String state,
        @NotBlank
        String lg
) {
}
