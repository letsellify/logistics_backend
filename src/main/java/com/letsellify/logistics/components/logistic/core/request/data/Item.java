package com.letsellify.logistics.components.logistic.core.request.data;


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
}
