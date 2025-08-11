package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.repository;

import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.entity.LogisticItemImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:2/9/25
 * Time:17:46
 */

@Repository
public interface LogisticItemImageRepository extends JpaRepository<LogisticItemImageEntity, String> {
    Optional<LogisticItemImageEntity> findByIdAndSenderId(String id, UUID senderId);
}
