package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.KycType;

/**
 * Author: Ahmad Buba
 * Date:8/14/25
 */


public record DispatcherKycResource(
        @JsonProperty("type")
        KycType kycType,
        @JsonProperty("number")
        String kycNumber
) {
}
