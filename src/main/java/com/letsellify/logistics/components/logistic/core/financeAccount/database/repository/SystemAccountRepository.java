package com.letsellify.logistics.components.logistic.core.financeAccount.database.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letsellify.logistics.components.logistic.core.financeAccount.database.entity.SystemAccountEntity;

/**
 * @author AHMAD BUBA
 * Date:1/27/25
 * Time:09:45
 */

@Repository
public interface SystemAccountRepository extends JpaRepository<SystemAccountEntity, UUID> {}
