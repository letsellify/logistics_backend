package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data;


import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.dataMapper.LogisticRequestDataMapper;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.entity.LogisticRequestEntity;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.LogisticRequestResources;
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
        boolean hasNext
) {
    public LogisticRequests(List<LogisticRequest> requests, Page<LogisticRequestEntity> entityPage) {
        this(requests, entityPage.getNumber(), entityPage.getTotalPages(), entityPage.getTotalElements(), entityPage.isFirst(), entityPage.isLast(), entityPage.isEmpty(), entityPage.hasNext());
    }

    public LogisticRequestResources getResource() {
        return LogisticRequestDataMapper.INSTANCE.dataToResources(this);
    }

    public LogisticRequestResources getVendorResource() {
        return LogisticRequestDataMapper.INSTANCE.dataToVendorResources(this);
    }
}
