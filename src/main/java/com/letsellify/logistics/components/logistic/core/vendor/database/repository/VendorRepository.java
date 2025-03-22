package com.letsellify.logistics.components.logistic.core.vendor.database.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.letsellify.logistics.components.logistic.core.vendor.database.entity.VendorEntity;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:15
 */

public interface VendorRepository extends JpaRepository<VendorEntity, UUID> {
    boolean existsByEmail(String email);
    Optional<VendorEntity> findByEmail(String email);
}
