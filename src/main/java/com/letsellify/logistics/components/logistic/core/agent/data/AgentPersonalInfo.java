package com.letsellify.logistics.components.logistic.core.agent.data;

import com.letsellify.logistics.components.logistic.core.agent.dataMapper.AgentDataMapper;
import com.letsellify.logistics.components.logistic.core.agent.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistic.core.agent.rest.resource.AgentPersonalInfoResource;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:13:46
 */

public record AgentPersonalInfo(
  String whatsAppPhone,
  String phone,
  String state,
  String lga,
  String address
) {

    public AgentPersonalInfo(final AgentEntity.PersonalInfoEmbeddable personalInfoEmbeddable) {
        this(
          personalInfoEmbeddable.getWhatsAppPhone(),
          personalInfoEmbeddable.getPhone(),
          personalInfoEmbeddable.getState(),
          personalInfoEmbeddable.getLga(),
          personalInfoEmbeddable.getAddress()
        );
    }

    public AgentPersonalInfoResource getResource() {
        return AgentDataMapper.INSTANCE.toResource(this);
    }

}
