package com.letsellify.logistics.components.logistics.core.agentManagement.data;


import com.letsellify.logistics.components.logistics.core.agentManagement.dataMapper.AgentDataMapper;
import com.letsellify.logistics.components.logistics.core.agentManagement.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentGuarantorResource;
import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorCareer;
import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorRelationship;

/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */


public record AgentGuarantor(
        String name,
        String state,
        String lga,
        String address,
        GuarantorRelationship relationship,
        String phoneNumber,
        String whatsAppNumber,
        String email,
        GuarantorCareer career
) {

    public AgentGuarantor(AgentEntity.AgentGuarantorEmbeddable agentGuarantorEmbeddable) {
        this(
               agentGuarantorEmbeddable.getFullName(),
               agentGuarantorEmbeddable.getState(),
               agentGuarantorEmbeddable.getLga(),
               agentGuarantorEmbeddable.getAddress(),
               agentGuarantorEmbeddable.getRelationship(),
               agentGuarantorEmbeddable.getPhoneNumber(),
               agentGuarantorEmbeddable.getWhatsAppNumber(),
               agentGuarantorEmbeddable.getEmail(),
               agentGuarantorEmbeddable.getCareer()
        );
    }

    public AgentGuarantorResource getResource() {
        return AgentDataMapper.INSTANCE.toResource(this);
    }
}
