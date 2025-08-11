package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data;


import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.dataMapper.LogisticRequestDataMapper;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.LogisticItemResource;

import java.util.Set;

/**
 * Author: Ahmad Buba
 * Date:8/5/25
 */


public record Item(
        String name,
        int quantity,
        String description,
        String fragility,
        int weight,
        Set<ItemCondition> conditions
) {
    public LogisticItemResource getResource() {
        return LogisticRequestDataMapper.INSTANCE.dataToResource(this);
    }
}
