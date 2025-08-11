package com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * @author AHMAD BUBA
 * Date:5/3/25
 * Time:18:03
 */

public record AgentContactInfoDto(
        @NotBlank(message = "Phone number cannot be empty.")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format.")
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits.")
        String phone,

        @NotBlank(message = "WhatsApp phone number cannot be empty.")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid WhatsApp phone number format.")
        @Size(min = 10, max = 15, message = "WhatsApp phone number must be between 10 and 15 digits.")
        String whatsAppPhone
) {
}
