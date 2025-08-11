package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @author AHMAD BUBA
 * Date:2/22/25
 * Time:21:31
 */

public record DispatcherPersonalInfoDto(
        @NotBlank(message = "Name cannot be empty.")
        String name,

        @NotBlank(message = "State cannot be empty.")
        String state,

        @NotBlank(message = "LGA cannot be empty.")
        String lga,

        @NotBlank(message = "Address cannot be empty.")
        String address
) {
}
