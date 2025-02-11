package com.letsellify.logistics.components.user.core.verificationCodeManagement.database.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.letsellify.logistics.components.user.core.verificationCodeManagement.database.entity.VerificationCodeEntity;

/**
 * @author AHMAD BUBA
 * Date:2/10/25
 * Time:10:34
 */

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, UUID> {
    // Find by email and code, only if not expired and not verified
    Optional<VerificationCodeEntity> findByUserEmailAndCodeAndExpiredFalseAndVerifiedFalse(String email, String code);

    // Expire all previous unverified codes for a user
    void deleteByUserEmailAndVerifiedFalseAndExpiredFalse(String email);

    @Transactional
    @Modifying
    @Query("DELETE FROM VerificationCodeEntity v WHERE v.expiresAt < CURRENT_TIMESTAMP")
    void deleteExpiredCodes();
}
