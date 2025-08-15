package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.KycType;

/**
 * Author: Ahmad Buba
 * Date:8/14/25
 */


public record DispatcherKycResource(
        KycType kycType,
        String kycNumber
) {
}
