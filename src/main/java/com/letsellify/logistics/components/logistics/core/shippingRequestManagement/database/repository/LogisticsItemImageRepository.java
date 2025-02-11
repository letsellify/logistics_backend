package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.database.entity.LogisticsItemImageEntity;

/**
 * @author AHMAD BUBA
 * Date:2/9/25
 * Time:17:46
 */

@Repository
public interface LogisticsItemImageRepository extends JpaRepository<LogisticsItemImageEntity, String> {
    Optional<LogisticsItemImageEntity> findByIdAndVendorUsername(String id, String vendorUsername);
}
