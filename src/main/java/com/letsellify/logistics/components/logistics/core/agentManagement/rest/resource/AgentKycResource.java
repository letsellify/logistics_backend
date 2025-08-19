package com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.KycType;

/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */


public record AgentKycResource(
        KycType kycType,
        String kycNumber
) {
}
