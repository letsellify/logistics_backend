package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto;


import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorCareer;
import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorRelationship;
import jakarta.validation.constraints.*;

/**
 * Author: Ahmad Buba
 * Date:8/12/25
 */


public record DispatcherGuarantorDto(
        @NotBlank(message = "Guarantor name must be provided")
        String fullName,
        @NotBlank(message = "Guarantor state must be provided")
        String state,
        @NotBlank(message = "Guarantor lga must be provided")
        String lga,
        @NotBlank(message = "Address of guarantor must be provided")
        String address,
        @NotNull(message = "Relationship must be provided")
        GuarantorRelationship relationship,
        @NotBlank(message = "Phone number of guarantor must be provided")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format.")
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits.")
        String phoneNumber,
        @NotBlank(message = "WhatsApp phone number cannot be empty.")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid WhatsApp phone number format.")
        @Size(min = 10, max = 15, message = "WhatsApp phone number must be between 10 and 15 digits.")
        String whatsAppNumber,
        @NotBlank(message = "Guarantor email must be provided")
        @Email
        String email,
        @NotNull(message = "Career must be provided")
        GuarantorCareer career
) {
}
