package com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @author AHMAD BUBA
 * Date:5/3/25
 * Time:18:02
 */

public record AgentPersonalInfoDto(
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
