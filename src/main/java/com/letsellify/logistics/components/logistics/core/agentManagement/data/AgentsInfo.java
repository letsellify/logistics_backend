package com.letsellify.logistics.components.logistics.core.agentManagement.data;


import com.letsellify.logistics.components.logistics.core.agentManagement.dataMapper.AgentDataMapper;
import com.letsellify.logistics.components.logistics.core.agentManagement.database.entity.AgentEntity;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentProfileInfoResources;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.DispatcherInfo;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.DispatcherProfileInfoResources;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */


public record AgentsInfo(
        List<AgentInfo> agents,
        int currentPage,
        int totalPages,
        long totalElements,
        boolean isPageFirst,
        boolean isPageLast,
        boolean isPageEmpty,
        boolean hasNext
) {
    public AgentsInfo(List<AgentInfo> agents , Page<AgentEntity> agentsPage) {
        this(agents, agentsPage.getNumber(), agentsPage.getTotalPages(), agentsPage.getTotalElements(), agentsPage.isFirst(), agentsPage.isLast(), agentsPage.isEmpty(), agentsPage.hasNext());
    }
    public AgentProfileInfoResources getResource() {
        return AgentDataMapper.INSTANCE.toProfileResources(this);
    }
}
