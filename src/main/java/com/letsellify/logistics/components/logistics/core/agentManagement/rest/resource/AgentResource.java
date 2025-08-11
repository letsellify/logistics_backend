package com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource;

/**
 * @author AHMAD BUBA
 * Date:2/24/25
 * Time:21:23
 */

public record AgentResource(
        String name,
        String email,
        String whatsAppPhone,
        String phone,
        String state,
        String lga,
        String address
) {
}
