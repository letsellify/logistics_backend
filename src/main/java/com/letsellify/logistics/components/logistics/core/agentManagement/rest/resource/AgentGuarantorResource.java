package com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource;


import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorCareer;
import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorRelationship;

/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */


public record AgentGuarantorResource(
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
}
