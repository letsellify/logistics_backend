package com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.repository;

import com.letsellify.logistics.components.logistics.core.financeAccountManagement.database.entity.SystemAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/27/25
 * Time:09:45
 */

@Repository
public interface SystemAccountRepository extends JpaRepository<SystemAccountEntity, UUID> {
}
