package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto;

import com.letsellify.logistics.common.validator.HasKyc;
import com.letsellify.logistics.common.validator.annotation.ValidKyc;
import jakarta.validation.constraints.NotNull;

/**
 * @author AHMAD BUBA
 * Date:5/3/25
 * Time:21:36
 */

@ValidKyc
public record DispatcherProfileDto(
        @NotNull
        DispatcherPersonalInfoDto personalInformation,
        @NotNull
        DispatcherContactInfoDto contactInformation,
        DispatchDetailDto businessInformation,
        @NotNull
        DispatcherGuarantorDto guarantorInformation,
        @NotNull
        KycDto kyc
) implements HasKyc {
    @Override
    public KycDto getKyc() {
        return this.kyc;
    }
}
