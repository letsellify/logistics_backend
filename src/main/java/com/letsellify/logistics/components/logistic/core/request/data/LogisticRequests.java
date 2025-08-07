package com.letsellify.logistics.components.logistic.core.request.data;


import com.letsellify.logistics.components.logistic.core.request.dataMapper.LogisticRequestDataMapper;
import com.letsellify.logistics.components.logistic.core.request.database.entity.LogisticRequestEntity;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.LogisticRequestResources;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.VendorLogisticRequestResources;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Author: Ahmad Buba
 * Date:8/7/25
 */


public record LogisticRequests(
        List<LogisticRequest> requests,
        int currentPage,
        int totalPages,
        long totalElements,
        boolean isPageFirst,
        boolean isPageLast,
        boolean isPageEmpty,
        boolean hasNext,
        int nextPage
) {
    public LogisticRequests(List<LogisticRequest> requests, Page<LogisticRequestEntity> entityPage) {
        this(requests, entityPage.getNumber(), entityPage.getTotalPages(), entityPage.getTotalElements(), entityPage.isFirst(), entityPage.isLast(), entityPage.isEmpty(), entityPage.hasNext(), entityPage.hasNext()? entityPage.getNumber() + 1 : entityPage.getNumber());
    }

    public LogisticRequestResources getResource() {
        return LogisticRequestDataMapper.INSTANCE.dataToResources(this);
    }

    public VendorLogisticRequestResources getVendorResource() {
        return LogisticRequestDataMapper.INSTANCE.dataToVendorResources(this);
    }
}
