package com.letsellify.logistics.components.logistics.core.agentManagement.data;


import com.letsellify.logistics.components.logistics.core.agentManagement.dataMapper.AgentDataMapper;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentKycResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.KycType;

/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */


public record AgentKyc(
        KycType kycType,
        String kycNumber
) {
    public AgentKycResource getResource() {
        return AgentDataMapper.INSTANCE.getResource(this);
    }
}
