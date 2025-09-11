package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto;


import jakarta.validation.constraints.NotBlank;

/**
 * Author: Ahmad Buba
 * Date:8/22/25
 */


public record LgaPreferenceDto(
        @NotBlank(message = "Pick up lga preference must be specified")
        String pickUpLga,
        @NotBlank(message = "Drop off lga preference must be specified")
        String dropOffLga
) {
}
