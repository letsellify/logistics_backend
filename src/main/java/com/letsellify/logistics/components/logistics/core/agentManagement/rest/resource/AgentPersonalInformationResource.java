package com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource;


/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */


public record AgentPersonalInformationResource(
        String name,
        String state,
        String lga,
        String address
) {
}
