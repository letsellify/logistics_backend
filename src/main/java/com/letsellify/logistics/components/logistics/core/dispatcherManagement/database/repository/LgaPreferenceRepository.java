package com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.repository;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.LgaPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Author: Ahmad Buba
 * Date:8/22/25
 */


@Repository
public interface LgaPreferenceRepository extends JpaRepository<LgaPreferenceEntity, Long> {
    List<LgaPreferenceEntity> findByDispatcherId(UUID dispatcherId);
}
