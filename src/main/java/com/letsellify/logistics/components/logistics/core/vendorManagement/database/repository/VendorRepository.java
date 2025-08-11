package com.letsellify.logistics.components.logistics.core.vendorManagement.database.repository;

import com.letsellify.logistics.components.logistics.core.vendorManagement.database.entity.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:15
 */

public interface VendorRepository extends JpaRepository<VendorEntity, UUID> {
    boolean existsByEmail(String email);

    Optional<VendorEntity> findByEmail(String email);
}
