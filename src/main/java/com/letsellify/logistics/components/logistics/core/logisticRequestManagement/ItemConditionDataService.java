package com.letsellify.logistics.components.logistics.core.logisticRequestManagement;


import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.ItemCondition;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.LogisticItemConditionResources;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Ahmad Buba
 * Date: 8/7/25
 */

@Service
@RequiredArgsConstructor
public class ItemConditionDataService {
    private final ItemConditionManager itemConditionManager;

    public LogisticItemConditionResources getItemConditions() {
        List<ItemCondition> dataList = this.itemConditionManager.getConditions();
        List<String> conditions = new ArrayList<>();
        for (ItemCondition itemCondition : dataList) {
            conditions.add(itemCondition.name());
        }
        return new LogisticItemConditionResources(conditions);
    }
}
