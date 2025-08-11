package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.repository;

import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.entity.LogisticRequestEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:06:27
 */

@Repository
public interface LogisticRequestRepository extends JpaRepository<LogisticRequestEntity, UUID> {
    Optional<LogisticRequestEntity> findByShippingRequestId(String shippingRequestId);

    Optional<LogisticRequestEntity> findByShippingRequestIdAndSenderId(String shippingRequestId, UUID senderId);

    Page<LogisticRequestEntity> findAllBySenderId(UUID senderId, Pageable pageable);

    Page<LogisticRequestEntity> findAllByDispatcherId(UUID dispatcherId, @NonNull Pageable pageable);

    Page<LogisticRequestEntity> findAllByAgentId(@NonNull UUID agentId, @NonNull Pageable pageable);
}
