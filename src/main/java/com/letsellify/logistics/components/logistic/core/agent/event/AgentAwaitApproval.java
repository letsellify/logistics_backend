package com.letsellify.logistics.components.logistic.core.agent.event;

import org.springframework.context.ApplicationEvent;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistic.core.agent.data.LogisticAgentInfo;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:13:55
 */

@Getter
public class AgentAwaitApproval extends ApplicationEvent {
    private final String agentName;
    private final String agentEmail;
    private final LogisticAppRole role;

    public AgentAwaitApproval(final LogisticAgentInfo agentInfo) {
        super(agentInfo);
        this.agentName = agentInfo.name();
        this.agentEmail = agentInfo.email();
        this.role = LogisticAppRole.AGENT;
    }

}
