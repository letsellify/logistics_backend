package com.letsellify.logistics.components.logistic.core.request.rest.resource;


import com.letsellify.logistics.components.logistic.core.request.data.Item;
import com.letsellify.logistics.components.logistic.core.request.data.ItemCondition;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Ahmad Buba
 * Date:8/6/25
 */


public record LogisticItemResource(
        String name,
        int quantity,
        String description,
        String fragility,
        int weight,
        Set<String> conditions
) {
    public LogisticItemResource(Item item) {
        this(item.name(), item.quantity(), item.description(), item.fragility(), item.weight(), item.conditions().stream().map(ItemCondition::name).collect(Collectors.toSet()));
    }
}
