package com.letsellify.logistics.components.logistics.core.agentManagement.data;


import com.letsellify.logistics.components.logistics.core.agentManagement.dataMapper.AgentDataMapper;
import com.letsellify.logistics.components.logistics.core.agentManagement.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentBusinessInformationResource;

/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */


public record AgentBusinessInformation(
        String name,
        String address,
        String state,
        String lga
) {

    public AgentBusinessInformation(AgentEntity.StoreDetailEmbeddable storeDetailEmbeddable) {
        this(
                storeDetailEmbeddable.getBusinessName(),
                storeDetailEmbeddable.getBusinessOfficeAddress(),
                storeDetailEmbeddable.getBusinessState(),
                storeDetailEmbeddable.getBusinessLga()
        );
    }

    public AgentBusinessInformationResource getResource() {
        return AgentDataMapper.INSTANCE.toResource(this);
    }
}
