package com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.database.repository;

import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.database.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:3/1/25
 * Time:12:40
 */

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, UUID> {
    Optional<BankAccountEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
