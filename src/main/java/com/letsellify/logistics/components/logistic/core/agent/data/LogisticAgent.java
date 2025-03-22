package com.letsellify.logistics.components.logistic.core.agent.data;

import com.letsellify.logistics.components.logistic.core.agent.dataMapper.AgentDataMapper;
import com.letsellify.logistics.components.logistic.core.agent.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistic.core.agent.rest.resource.LogisticAgentResource;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:06:40
 */

@Data
@NoArgsConstructor
@Embeddable
public class LogisticAgent {
  @Column(name = "agent_name")
  private String name;
  @Column(name = "agent_email")
  private String email;
  @Column(name = "agent_whatsAppPhone")
  private String whatsAppPhone;
  @Column(name = "agent_phone")
  private String phone;
  @Column(name = "agent_state")
  private String state;
  @Column(name = "agent_lga")
  private String lga;
  @Column(name = "agent_address")
  private String address;

    public LogisticAgent(final AgentEntity agentEntity) {
          this.name = agentEntity.getName();
          this.email = agentEntity.getEmail();
          this.whatsAppPhone = agentEntity.getPersonalInfo().getWhatsAppPhone();
          this.phone = agentEntity.getPersonalInfo().getPhone();
          this.state = agentEntity.getPersonalInfo().getState();
          this.lga = agentEntity.getPersonalInfo().getLga();
          this.address = agentEntity.getPersonalInfo().getAddress();
    }

    public LogisticAgentResource getResource() {
        return AgentDataMapper.INSTANCE.toResource(this);
    }
}
