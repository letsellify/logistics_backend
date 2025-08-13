package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.KycType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Author: Ahmad Buba
 * Date:8/13/25
 */


public record KycDto(
        @NotNull(message = "Kyc type must be specified") KycType kyc, @Size(min = 11, max = 11, message = "Number must be 11") String number
) {
}
