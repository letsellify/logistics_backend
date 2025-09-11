package com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.repository;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity.LogisticsAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:10:46
 */

public interface LogisticsAccountRepository extends JpaRepository<LogisticsAccountEntity, UUID> {
    Optional<LogisticsAccountEntity> findByUserId(UUID userId);

    Optional<LogisticsAccountEntity> findByUserIdAndAppRole(UUID userId, LogisticAppRole appRole);

    Optional<LogisticsAccountEntity> findByAppRole(LogisticAppRole appRole);
}
