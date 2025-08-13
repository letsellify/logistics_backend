package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.validator.annotation.ValidKyc;
import jakarta.validation.constraints.NotNull;

/**
 * @author AHMAD BUBA
 * Date:5/3/25
 * Time:21:36
 */

@ValidKyc
public record DispatcherProfileDto(
        @NotNull
        DispatcherPersonalInfoDto personalInfo,
        @NotNull
        DispatcherContactInfoDto contactInfo,
        @NotNull
        DispatchDetailDto dispatchDetail,
        @NotNull
        DispatcherGuarantorDto guarantorInfo,
        @NotNull
        KycDto kyc
) {
}
