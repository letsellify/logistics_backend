package com.letsellify.logistics.components.logistics.core.agentManagement.data;


import com.letsellify.logistics.components.logistics.core.agentManagement.dataMapper.AgentDataMapper;
import com.letsellify.logistics.components.logistics.core.agentManagement.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentPersonalInformationResource;

/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */


public record AgentPersonalInformation(
        String name,
        String state,
        String lga,
        String address
) {

    public AgentPersonalInformation(AgentEntity.PersonalInfoEmbeddable personalInfoEmbeddable) {
        this(
                personalInfoEmbeddable.getName(),
                personalInfoEmbeddable.getState(),
                personalInfoEmbeddable.getLga(),
                personalInfoEmbeddable.getAddress()
        );
    }

    public AgentPersonalInformationResource getResource() {
        return AgentDataMapper.INSTANCE.getResource(this);
    }
}
