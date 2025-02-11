package com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.letsellify.logistics.common.data.LogisticsAppRole;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity.LogisticsAccountEntity;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:10:46
 */

public interface LogisticsAccountRepository extends JpaRepository<LogisticsAccountEntity, UUID> {
    Optional<LogisticsAccountEntity> findByUserEmail(String email);
    Optional<LogisticsAccountEntity> findByUserEmailAndAppRole(String email, LogisticsAppRole appRole);
}
