package com.letsellify.logistics.components.logistics.core.agentManagement.data;

import com.letsellify.logistics.components.logistics.core.agentManagement.dataMapper.AgentDataMapper;
import com.letsellify.logistics.components.logistics.core.agentManagement.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentResource;

import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:06:40
 */

public record Agent(
        UUID id,
        String name,
        String email,
        String whatsAppPhone,
        String phone,
        String state,
        String lga,
        String address,
        String storeState,
        String storeLga,
        String storeAddress
) {
    public Agent(final AgentEntity agentEntity) {
        this(agentEntity.getId(), agentEntity.getPersonalInfo()
                .getName(), agentEntity.getEmail(), agentEntity.getContactInfo().getWhatsAppPhone(), agentEntity.getContactInfo().getPhone(), agentEntity.getPersonalInfo().getHomeState(), agentEntity.getPersonalInfo().getHomeLga(), agentEntity.getPersonalInfo().getHomeAddress(), agentEntity.getStoreDetail().getStoreState(), agentEntity.getStoreDetail().getStoreLga(), agentEntity.getStoreDetail().getStoreAddress());
    }

    public AgentResource getResource() {
        return AgentDataMapper.INSTANCE.toResource(this);
    }
}

