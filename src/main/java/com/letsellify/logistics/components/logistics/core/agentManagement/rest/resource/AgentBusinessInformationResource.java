package com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource;


/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */


public record AgentBusinessInformationResource(
        String name,
        String address,
        String state,
        String lga
) {
}
