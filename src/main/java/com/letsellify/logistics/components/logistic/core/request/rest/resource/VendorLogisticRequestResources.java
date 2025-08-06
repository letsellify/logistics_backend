package com.letsellify.logistics.components.logistic.core.request.rest.resource;


import java.util.List;

/**
 * Author: Ahmad Buba
 * Date:8/6/25
 */


public record VendorLogisticRequestResources(
        List<VendorLogisticRequestResource> requests,
        int totalRequests
) {
    public VendorLogisticRequestResources(List<VendorLogisticRequestResource> requestResources) {
        this(requestResources, requestResources.size());
    }
}
