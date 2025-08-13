package com.letsellify.logistics.components.logistics.core.dispatcherManagement.data;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.DispatcherProfileInfoResources;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Author: Ahmad Buba
 * Date:8/12/25
 */


public record DispatchersInfo(
        List<DispatcherInfo> dispatchers,
        int currentPage,
        int totalPages,
        long totalElements,
        boolean isPageFirst,
        boolean isPageLast,
        boolean isPageEmpty,
        boolean hasNext
) {
    public DispatchersInfo(List<DispatcherInfo> dispatchers ,Page<DispatcherEntity> dispatchersPage) {
        this(dispatchers, dispatchersPage.getNumber(), dispatchersPage.getTotalPages(), dispatchersPage.getTotalElements(), dispatchersPage.isFirst(), dispatchersPage.isLast(), dispatchersPage.isEmpty(), dispatchersPage.hasNext());
    }
    public DispatcherProfileInfoResources getResource() {
        return DispatcherMapper.INSTANCE.getResources(this);
    }

}
