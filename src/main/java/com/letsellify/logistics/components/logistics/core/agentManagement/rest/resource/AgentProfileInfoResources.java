package com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;

import java.util.List;

/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */


public record AgentProfileInfoResources(
        List<AgentProfileInfoResource> agents,
        int currentPage,
        int totalPages,
        long totalElements,
        boolean isPageFirst,
        boolean isPageLast,
        boolean isPageEmpty,
        boolean hasNext
) {
}
