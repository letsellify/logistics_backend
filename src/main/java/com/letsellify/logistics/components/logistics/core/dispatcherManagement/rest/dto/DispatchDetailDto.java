package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author AHMAD BUBA
 * Date:5/3/25
 * Time:21:36
 */

public record DispatchDetailDto(
        @NotNull
        String name,
        @NotNull
        String address,
        @NotBlank(message = "State cannot be empty.")
        String state,
        @NotBlank(message = "LGA cannot be empty.")
        String lga
) {
}
