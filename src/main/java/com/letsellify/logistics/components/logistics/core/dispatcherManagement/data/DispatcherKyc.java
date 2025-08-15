package com.letsellify.logistics.components.logistics.core.dispatcherManagement.data;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherKycResource;

/**
 * Author: Ahmad Buba
 * Date:8/14/25
 */


public record DispatcherKyc(
        KycType kycType,
        String kycNumber
) {
    public DispatcherKycResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }
}
