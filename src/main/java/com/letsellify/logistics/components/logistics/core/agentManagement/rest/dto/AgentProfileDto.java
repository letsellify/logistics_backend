package com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto;


import com.letsellify.logistics.common.validator.HasKyc;
import com.letsellify.logistics.common.validator.annotation.ValidKyc;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.KycDto;
import jakarta.validation.constraints.NotNull;

/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */


@ValidKyc
public record AgentProfileDto(
        @NotNull(message = "Personal information must be present")
        AgentPersonalInfoDto personalInformation,
        @NotNull
        AgentContactInfoDto contactInformation,
        @NotNull
        AgentBusinessInformationDto businessInformation,
        @NotNull
        AgentGuarantorDto guarantorInformation,
        @NotNull
        KycDto kyc
) implements HasKyc {
    @Override
    public KycDto getKyc() {
        return kyc;
    }
}
