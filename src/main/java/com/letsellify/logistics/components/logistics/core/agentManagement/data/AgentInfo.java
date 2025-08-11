package com.letsellify.logistics.components.logistics.core.agentManagement.data;

import com.letsellify.logistics.components.logistics.core.agentManagement.dataMapper.AgentDataMapper;
import com.letsellify.logistics.components.logistics.core.agentManagement.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentInfoResource;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.KycDocumentType;

import java.util.Map;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:03:42
 */

public record AgentInfo(
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
        UUID kycId,
        Map<KycDocumentType, String> kycs
) {
    public AgentInfo(final AgentEntity entity, final Map<KycDocumentType, String> kycs) {
        this(
                entity.getEmail(),
                entity.getPersonalInfo().getName(),
                entity.getPersonalInfo().getHomeState(),
                entity.getPersonalInfo().getHomeLga(),
                entity.getPersonalInfo().getHomeAddress(),
                entity.getContactInfo().getWhatsAppPhone(),
                entity.getContactInfo().getPhone(),
                entity.getStoreDetail().getStoreState(),
                entity.getStoreDetail().getStoreLga(),
                entity.getStoreDetail().getStoreAddress(),
                entity.getKycId(),
                kycs
        );
    }

    public AgentInfoResource getResource() {
        return AgentDataMapper.INSTANCE.toResource(this);
    }
}
