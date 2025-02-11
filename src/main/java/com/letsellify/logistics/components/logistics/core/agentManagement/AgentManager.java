package com.letsellify.logistics.components.logistics.core.agentManagement;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.letsellify.logistics.components.user.core.userManagement.event.UserOfRoleAgentCreated;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:00
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class AgentManager {


    @EventListener
    public void on(final UserOfRoleAgentCreated event) {
        log.info("Handling AgentCreatedEvent for email: {}", event.getUserEmail());
        // Agent-specific logic here, e.g., assigning delivery zones
    }
}
