package com.letsellify.logistics.components.logistic.core.request;


import com.letsellify.logistics.components.logistic.core.request.data.ItemCondition;
import com.letsellify.logistics.components.logistic.core.request.database.entity.ConditionEntity;
import com.letsellify.logistics.components.logistic.core.request.database.repository.ConditionRepository;
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
