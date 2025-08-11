package com.letsellify.logistics.components.logistics.core.kycManagement.database.repository;

import com.letsellify.logistics.components.logistics.core.kycManagement.database.entity.KycEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author AHMAD BUBA
 * Date:2/3/25
 * Time:08:59
 */

public interface KycRepository extends JpaRepository<KycEntity, String> {
}
