package com.letsellify.logistics.components.logistics.core.kyc.database.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letsellify.logistics.components.logistics.core.kyc.database.entity.KycEntity;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:12:49
 */

@Repository
public interface KycRepository extends JpaRepository<KycEntity, UUID> {
    Optional<KycEntity> findByUserId(UUID userId);
}
