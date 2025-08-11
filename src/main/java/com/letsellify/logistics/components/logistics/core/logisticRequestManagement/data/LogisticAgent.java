package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data;

import com.letsellify.logistics.components.logistics.core.agentManagement.data.Agent;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.dataMapper.LogisticRequestDataMapper;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.LogisticAgentResource;

import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:6/22/25
 * Time:21:13
 */


public record LogisticAgent(
        UUID id,
        String email,
        String name,
        String state,
        String lga,
        String address,
        String whatsAppPhone,
        String phone,
        String storeState,
        String storeLga,
        String storeAddress
) {
    public LogisticAgent(Agent agent) {
        this(agent.id(), agent.email(), agent.name(), agent.state(), agent.lga(), agent.address(), agent.whatsAppPhone(), agent.phone(), agent.storeState(), agent.storeLga(), agent.storeAddress());
    }

    public LogisticAgentResource getResource() {
        return LogisticRequestDataMapper.INSTANCE.dataToResource(this);
    }
}
