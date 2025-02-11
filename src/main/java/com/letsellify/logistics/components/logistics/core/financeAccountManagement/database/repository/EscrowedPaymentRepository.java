package com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity.EscrowedPaymentEntity;

/**
 * @author AHMAD BUBA
 * Date:1/26/25
 * Time:08:17
 */

public interface EscrowedPaymentRepository extends JpaRepository<EscrowedPaymentEntity, UUID> {
    Optional<EscrowedPaymentEntity> findByShippingId(UUID shippingId);
}
