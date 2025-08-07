package com.letsellify.logistics.components.logistic.core.request.rest.resource;


import com.letsellify.logistics.components.logistic.core.request.data.LogisticRequests;

import java.util.List;

/**
 * Author: Ahmad Buba
 * Date:8/6/25
 */


public record VendorLogisticRequestResources(
        List<VendorLogisticRequestResource> requests,
        int currentPage,
        int totalPages,
        long totalElements,
        boolean isPageFirst,
        boolean isPageLast,
        boolean isPageEmpty,
        boolean hasNext,
        int nextPage
) {
    public VendorLogisticRequestResources(LogisticRequests logisticRequests) {
        this(logisticRequests.getVendorResource().requests(), logisticRequests.currentPage(), logisticRequests.totalPages(),logisticRequests.totalElements(),logisticRequests.isPageFirst(), logisticRequests.isPageLast(), logisticRequests.isPageEmpty(), logisticRequests.hasNext(), logisticRequests.nextPage());
    }
}
