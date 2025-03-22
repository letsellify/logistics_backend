package com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.database.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.database.entity.PaystackPaymentEntity;

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
