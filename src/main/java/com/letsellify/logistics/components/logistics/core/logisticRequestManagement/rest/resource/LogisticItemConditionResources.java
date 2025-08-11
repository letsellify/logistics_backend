package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource;


import java.util.List;

/**
 * Author: Ahmad Buba
 * Date:8/6/25
 */


public record LogisticItemConditionResources(
        List<String> conditions,
        int totalConditions
) {
    public LogisticItemConditionResources(List<String> condition) {
        this(condition, condition.size());
    }
}
