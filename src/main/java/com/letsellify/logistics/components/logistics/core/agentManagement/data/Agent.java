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
        this(agentEntity.getId(), agentEntity.getPersonalInformation()
                .getName(), agentEntity.getEmail(), agentEntity.getContactInformation().getWhatsAppPhone(), agentEntity.getContactInformation().getPhone(), agentEntity.getPersonalInformation().getState(), agentEntity.getPersonalInformation().getLga(), agentEntity.getPersonalInformation().getAddress(), agentEntity.getBusinessInformation().getBusinessState(), agentEntity.getBusinessInformation().getBusinessLga(), agentEntity.getBusinessInformation().getBusinessOfficeAddress());
    }

    public AgentResource getResource() {
        return AgentDataMapper.INSTANCE.toResource(this);
    }
}

