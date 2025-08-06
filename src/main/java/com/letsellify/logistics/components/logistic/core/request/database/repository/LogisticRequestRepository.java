package com.letsellify.logistics.components.logistic.core.request.database.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.letsellify.logistics.components.logistic.core.request.data.LogisticRequest;
import lombok.NonNull;
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
    Optional<LogisticRequestEntity> findByShippingRequestIdAndSenderId(String shippingRequestId, UUID senderId);

    List<LogisticRequestEntity> findBySenderId(UUID senderId);
}
