package com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.database.repository;

import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.database.entity.PaystackPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:21:23
 */

@Repository
public interface PaystackPaymentRepository extends JpaRepository<PaystackPaymentEntity, UUID> {
    Optional<PaystackPaymentEntity> findByAccessCode(String accessCode);

    Optional<PaystackPaymentEntity> findByReference(String reference);
}
