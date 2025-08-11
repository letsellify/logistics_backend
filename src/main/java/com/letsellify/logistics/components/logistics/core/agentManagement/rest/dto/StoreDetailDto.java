package com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * @author AHMAD BUBA
 * Date:5/3/25
 * Time:18:04
 */

public record StoreDetailDto(
        @NotBlank(message = "State cannot be empty.")
        String state,

        @NotBlank(message = "LGA cannot be empty.")
        String lga,

        @JsonProperty("store_address")
        @NotBlank(message = "Address cannot be empty.")
        String address
) {
}
