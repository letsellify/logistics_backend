package com.letsellify.logistics.components.logistic.core.nigerianBankAccount.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letsellify.logistics.components.logistic.core.nigerianBankAccount.database.entity.BankEntity;

/**
 * @author AHMAD BUBA
 * Date:2/28/25
 * Time:09:37
 */

@Repository
public interface BankRepository extends JpaRepository<BankEntity,Integer> {
    Optional<BankEntity> findByName(String name);
    Optional<BankEntity> findByCode(String code);
    Optional<BankEntity> findByLongcode(String longCode);
}
