package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.DispatcherInfo;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;

import java.util.List;

/**
 * Author: Ahmad Buba
 * Date:8/12/25
 */


public record DispatcherProfileInfoResources(
        List<DispatcherProfileInfoResource> dispatchers,
        int currentPage,
        int totalPages,
        long totalElements,
        boolean isPageFirst,
        boolean isPageLast,
        boolean isPageEmpty,
        boolean hasNext
) {
}
