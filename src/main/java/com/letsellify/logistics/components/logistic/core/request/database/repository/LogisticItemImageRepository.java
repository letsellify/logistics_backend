package com.letsellify.logistics.components.logistic.core.request.database.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letsellify.logistics.components.logistic.core.request.database.entity.LogisticItemImageEntity;

/**
 * @author AHMAD BUBA
 * Date:2/9/25
 * Time:17:46
 */

@Repository
public interface LogisticItemImageRepository extends JpaRepository<LogisticItemImageEntity, String> {
    Optional<LogisticItemImageEntity> findByIdAndSenderId(String id, UUID senderId);
}
