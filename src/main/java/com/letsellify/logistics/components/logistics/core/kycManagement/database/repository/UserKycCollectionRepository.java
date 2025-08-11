package com.letsellify.logistics.components.logistics.core.kycManagement.database.repository;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistics.core.kycManagement.database.entity.UserKycCollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:12:49
 */

@Repository
public interface UserKycCollectionRepository extends JpaRepository<UserKycCollectionEntity, UUID> {
    //    Optional<KycEntity> findByUserId(UUID userId);
    Optional<UserKycCollectionEntity> findByUserEmail(String email);

    Optional<UserKycCollectionEntity> findByUserEmailAndUserType(String email, LogisticAppRole userType);
//    Optional<UserKycCollectionEntity> findByUserEmailAndKycDocument(String userEmail, KycDocument kycDocument);
}
