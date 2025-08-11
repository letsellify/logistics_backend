package com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:13:51
 */

public record AgentProfileInfoResource(
        String email,
        String name,
        String homeState,
        String homeLga,
        String homeAddress,
        String whatsAppPhone,
        String phone,
        String storeState,
        String storeLga,
        String storeAddress
) {
}
