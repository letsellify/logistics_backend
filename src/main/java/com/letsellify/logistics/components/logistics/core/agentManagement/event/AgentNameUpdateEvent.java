package com.letsellify.logistics.components.logistics.core.agentManagement.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author AHMAD BUBA
 * Date:5/3/25
 * Time:19:52
 */

@Getter
public class AgentNameUpdateEvent extends ApplicationEvent {
    private final String agentEmail;
    private final String agentNameBeforeUpdate;
    private final String agentNameAfterUpdate;

    public AgentNameUpdateEvent(final String agentEmail, final String agentNameBeforeUpdate, final String agentNameAfterUpdate) {
        super(agentEmail);
        this.agentEmail = agentEmail;
        this.agentNameBeforeUpdate = agentNameBeforeUpdate;
        this.agentNameAfterUpdate = agentNameAfterUpdate;
    }

}
