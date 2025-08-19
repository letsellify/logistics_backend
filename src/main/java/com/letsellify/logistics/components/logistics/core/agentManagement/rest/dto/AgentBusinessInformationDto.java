package com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */


public record AgentBusinessInformationDto(
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
