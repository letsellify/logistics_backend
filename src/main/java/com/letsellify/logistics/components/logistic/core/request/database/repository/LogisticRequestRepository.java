package com.letsellify.logistics.components.logistic.core.request.database.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letsellify.logistics.components.logistic.core.request.database.entity.LogisticRequestEntity;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:06:27
 */

@Repository
public interface LogisticRequestRepository extends JpaRepository<LogisticRequestEntity, UUID> {
    Optional<LogisticRequestEntity> findByShippingRequestId(String shippingRequestId);
}
