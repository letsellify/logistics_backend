package com.letsellify.logistics.components.logistics.core.logisticRequestManagement;


import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.ItemCondition;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.entity.ConditionEntity;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.repository.ConditionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Ahmad Buba
 * Date: 8/7/25
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemConditionManager {
    private final ConditionRepository conditionRepository;

    public List<ItemCondition> getConditions() {
        List<ConditionEntity> entityList = conditionRepository.findAll();
        List<ItemCondition> conditions = new ArrayList<>();
        for (ConditionEntity entity : entityList) {
            conditions.add(new ItemCondition(entity.getName()));
        }
        return conditions;
    }
}
