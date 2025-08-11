package com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.repository;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherAvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:04:27
 */

@Repository
public interface DispatcherAvailabilityRepository extends JpaRepository<DispatcherAvailabilityEntity, UUID> {
}
