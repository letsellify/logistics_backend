package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource;


import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.LogisticRequests;

import java.util.List;

/**
 * Author: Ahmad Buba
 * Date:8/6/25
 */


public record LogisticRequestResources(
        List<LogisticRequestResource> requests,
        int currentPage,
        int totalPages,
        long totalElements,
        boolean isPageFirst,
        boolean isPageLast,
        boolean isPageEmpty,
        boolean hasNext
) {
    public LogisticRequestResources(LogisticRequests logisticRequests) {
        this(logisticRequests.getVendorResource().requests(), logisticRequests.currentPage(), logisticRequests.totalPages(), logisticRequests.totalElements(), logisticRequests.isPageFirst(), logisticRequests.isPageLast(), logisticRequests.isPageEmpty(), logisticRequests.hasNext());
    }
}
