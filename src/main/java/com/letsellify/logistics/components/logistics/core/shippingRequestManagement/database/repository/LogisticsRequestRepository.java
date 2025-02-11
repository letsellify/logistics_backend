package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.database.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.database.entity.LogisticsRequestEntity;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:06:27
 */

@Repository
public interface LogisticsRequestRepository extends JpaRepository<LogisticsRequestEntity, UUID> {
    Optional<LogisticsRequestEntity> findByShippingRequestId(String shippingRequestId);
}
