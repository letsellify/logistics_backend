package com.letsellify.logistics.components.logistics.core.agentManagement.data;

import com.letsellify.logistics.components.logistics.core.agentManagement.dataMapper.AgentDataMapper;
import com.letsellify.logistics.components.logistics.core.agentManagement.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentInfoResource;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.*;
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
        String profilePicture,
        AgentPersonalInformation personalInformation,
        AgentContactInformation contactInformation,
        AgentBusinessInformation businessInformation,
        AgentGuarantor guarantor,
        AgentKyc kyc
) {
    public AgentInfo(final String profilePicture, final AgentEntity agentEntity) {
        this(
                agentEntity.getEmail(),
                profilePicture,
                agentEntity.getPersonalInformation() != null ? new AgentPersonalInformation(agentEntity.getPersonalInformation()) : null,
                agentEntity.getContactInformation() != null ? new AgentContactInformation(agentEntity.getContactInformation()) : null,
                agentEntity.getBusinessInformation() != null ? new AgentBusinessInformation(agentEntity.getBusinessInformation()) : null,
                agentEntity.getGuarantor() != null ? new AgentGuarantor(agentEntity.getGuarantor()) : null,
                agentEntity.getKycType() != null && agentEntity.getKycNumber() != null ? new AgentKyc(agentEntity.getKycType(), agentEntity.getKycNumber()) : null
        );
    }

    public AgentProfileInfoResource getResource() {
        return AgentDataMapper.INSTANCE.toProfileResource(this);
    }
}
