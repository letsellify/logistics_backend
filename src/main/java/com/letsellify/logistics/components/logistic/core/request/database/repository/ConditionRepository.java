package com.letsellify.logistics.components.logistic.core.request.database.repository;


import com.letsellify.logistics.components.logistic.core.request.database.entity.ConditionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Author: Ahmad Buba
 * Date:8/5/25
 */


public interface ConditionRepository extends JpaRepository<ConditionEntity, Long> {
    Optional<ConditionEntity> findByName(String name);
}
