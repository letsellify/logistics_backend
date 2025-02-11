package com.letsellify.logistics.components.logistics.core.kycManagement.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.letsellify.logistics.components.logistics.core.kycManagement.database.entity.KycEntity;

/**
 * @author AHMAD BUBA
 * Date:2/3/25
 * Time:08:59
 */

public interface KycRepository extends JpaRepository<KycEntity, String> {}
