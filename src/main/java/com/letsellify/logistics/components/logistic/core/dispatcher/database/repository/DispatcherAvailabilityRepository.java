package com.letsellify.logistics.components.logistic.core.dispatcher.database.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letsellify.logistics.components.logistic.core.dispatcher.database.entity.DispatcherAvailabilityEntity;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:04:27
 */

@Repository
public interface DispatcherAvailabilityRepository extends JpaRepository<DispatcherAvailabilityEntity, UUID> {}
