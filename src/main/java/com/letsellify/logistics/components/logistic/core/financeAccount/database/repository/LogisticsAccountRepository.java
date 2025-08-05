package com.letsellify.logistics.components.logistic.core.financeAccount.database.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistic.core.financeAccount.database.entity.LogisticsAccountEntity;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:10:46
 */

public interface LogisticsAccountRepository extends JpaRepository<LogisticsAccountEntity, UUID> {
    Optional<LogisticsAccountEntity> findByUserId(UUID userId);
    Optional<LogisticsAccountEntity> findByUserIdAndAppRole(UUID userId, LogisticAppRole appRole);
}
