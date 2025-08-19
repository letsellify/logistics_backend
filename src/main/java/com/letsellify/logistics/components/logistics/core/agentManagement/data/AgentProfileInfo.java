package com.letsellify.logistics.components.logistics.core.agentManagement.data;

import com.letsellify.logistics.components.logistics.core.agentManagement.dataMapper.AgentDataMapper;
import com.letsellify.logistics.components.logistics.core.agentManagement.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentProfileInfoResource;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:13:46
 */

public record AgentProfileInfo(
        String email,
        String name,
        String homeState,
        String homeLga,
        String homeAddress,
        String whatsAppPhone,
        String phone,
        String storeState,
        String storeLga,
        String storeAddress
) {

    public AgentProfileInfo(final AgentEntity agentEntity) {
        this(
                agentEntity.getEmail(),
                agentEntity.getPersonalInformation().getName(),
                agentEntity.getPersonalInformation().getState(),
                agentEntity.getPersonalInformation().getLga(),
                agentEntity.getPersonalInformation().getAddress(),
                agentEntity.getContactInformation().getWhatsAppPhone(),
                agentEntity.getContactInformation().getPhone(),
                agentEntity.getBusinessInformation().getBusinessState(),
                agentEntity.getBusinessInformation().getBusinessLga(),
                agentEntity.getBusinessInformation().getBusinessOfficeAddress()
        );
    }

    public AgentProfileInfoResource getResource() {
        return AgentDataMapper.INSTANCE.toResource(this);
    }

}
