package com.letsellify.logistics.components.logistics.core.agentManagement.event;

import com.letsellify.logistics.common.data.LogisticAppRole;

import com.letsellify.logistics.components.logistics.core.agentManagement.data.AgentInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

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

    public AgentAwaitApproval(final AgentInfo agentInfo) {
        super(agentInfo);
        this.agentName = agentInfo.name();
        this.agentEmail = agentInfo.email();
        this.role = LogisticAppRole.AGENT;
    }

}
