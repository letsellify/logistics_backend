package com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource;

import com.letsellify.logistics.components.logistics.core.kycManagement.data.KycDocumentType;

import java.util.Map;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:03:47
 */

public record AgentInfoResource(
        String email,
        String name,
        String homeState,
        String homeLga,
        String homeAddress,
        String whatsAppPhone,
        String phone,
        String storeState,
        String storeLga,
        String storeAddress,
        Map<KycDocumentType, String> kycs
) {
}
