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
                agentEntity.getPersonalInfo().getName(),
                agentEntity.getPersonalInfo().getHomeState(),
                agentEntity.getPersonalInfo().getHomeLga(),
                agentEntity.getPersonalInfo().getHomeAddress(),
                agentEntity.getContactInfo().getWhatsAppPhone(),
                agentEntity.getContactInfo().getPhone(),
                agentEntity.getStoreDetail().getStoreState(),
                agentEntity.getStoreDetail().getStoreLga(),
                agentEntity.getStoreDetail().getStoreAddress()
        );
    }

    public AgentProfileInfoResource getResource() {
        return AgentDataMapper.INSTANCE.toResource(this);
    }

}
