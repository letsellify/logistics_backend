package com.letsellify.logistics.components.logistics.core.agentManagement.data;


import com.letsellify.logistics.components.logistics.core.agentManagement.dataMapper.AgentDataMapper;
import com.letsellify.logistics.components.logistics.core.agentManagement.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentContactInformationResource;

/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */


public record AgentContactInformation(
        String whatsAppPhone,
        String phone
) {
    public AgentContactInformation(AgentEntity.ContactInfoEmbeddable contactInfoEmbeddable) {
        this(
                contactInfoEmbeddable.getWhatsAppPhone(),
                contactInfoEmbeddable.getPhone()
        );
    }

    public AgentContactInformationResource getResource() {
        return AgentDataMapper.INSTANCE.toResource(this);
    }
}
