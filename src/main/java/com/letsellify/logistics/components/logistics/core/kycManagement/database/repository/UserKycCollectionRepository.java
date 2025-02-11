package com.letsellify.logistics.components.logistics.core.kycManagement.database.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letsellify.logistics.components.logistics.core.kycManagement.database.entity.UserKycCollectionEntity;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:12:49
 */

@Repository
public interface UserKycCollectionRepository extends JpaRepository<UserKycCollectionEntity, UUID> {
//    Optional<KycEntity> findByUserId(UUID userId);
    Optional<UserKycCollectionEntity> findByUserEmail(String email);
//    Optional<UserKycCollectionEntity> findByUserEmailAndKycDocument(String userEmail, KycDocument kycDocument);
}
