package com.letsellify.logistics.components.logistic.core.agent.data;

import java.util.Map;
import java.util.UUID;

import com.letsellify.logistics.components.logistic.core.agent.dataMapper.AgentDataMapper;
import com.letsellify.logistics.components.logistic.core.agent.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistic.core.agent.rest.resource.LogisticAgentInfoResource;
import com.letsellify.logistics.components.logistic.core.kyc.data.KycDocumentType;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:03:42
 */

public record LogisticAgentInfo(
  String email,
  String name,
  UUID kycId,
  String whatsAppPhone,
  String phone,
  String state,
  String lga,
  String address,
  Map<KycDocumentType,String> kycs
) {
    public LogisticAgentInfo(final AgentEntity entity, final Map<KycDocumentType,String> kycs) {
        this(
          entity.getEmail(),
          entity.getName(),
          entity.getKycId(),
          entity.getPersonalInfo().getWhatsAppPhone(),
          entity.getPersonalInfo().getPhone(),
          entity.getPersonalInfo().getState(),
          entity.getPersonalInfo().getLga(),
          entity.getPersonalInfo().getAddress(),
          kycs
        );
    }

    public LogisticAgentInfoResource getResource() {
        return AgentDataMapper.INSTANCE.toResource(this);
    }
}
