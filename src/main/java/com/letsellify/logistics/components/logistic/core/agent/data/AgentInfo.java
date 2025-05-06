package com.letsellify.logistics.components.logistic.core.agent.data;

import com.letsellify.logistics.components.logistic.core.agent.dataMapper.AgentDataMapper;
import com.letsellify.logistics.components.logistic.core.agent.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistic.core.agent.rest.resource.AgentPersonalInfoResource;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:13:46
 */

public record AgentInfo(
  String whatsAppPhone,
  String phone,
  String state,
  String lga,
  String address
) {

    public AgentInfo(final AgentEntity agentEntity) {
        this(
          agentEntity.getContactInfo().getWhatsAppPhone(),
          agentEntity.getContactInfo().getPhone(),
          agentEntity.getPersonalInfo().getHomeState(),
          agentEntity.getPersonalInfo().getHomeLga(),
          agentEntity.getPersonalInfo().getHomeAddress()
        );
    }

    public AgentPersonalInfoResource getResource() {
        return AgentDataMapper.INSTANCE.toResource(this);
    }

}
