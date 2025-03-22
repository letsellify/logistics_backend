package com.letsellify.logistics.components.logistic.core.dispatcher.database.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letsellify.logistics.components.logistic.core.dispatcher.database.entity.DispatcherEntity;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:04:26
 */

@Repository
public interface DispatcherRepository extends JpaRepository<DispatcherEntity, UUID> {
    Optional<DispatcherEntity> findByEmail(String email);
    List<DispatcherEntity> findByCurrentlyAcceptingDeliveryAndApprove(boolean currentlyAcceptingDelivery, boolean approve);
}
