package com.letsellify.logistics.components.logistic.core.agent.database.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letsellify.logistics.components.logistic.core.agent.database.entity.AgentEntity;

/**
 * @author AHMAD BUBA
 * Date:2/24/25
 * Time:21:17
 */

@Repository
public interface AgentRepository extends JpaRepository<AgentEntity, UUID> {
    Optional<AgentEntity> findByEmail(String email);
    List<AgentEntity> findByCurrentlyAcceptingStorageAndApprove(boolean currentlyAcceptingDelivery, boolean approve);
}
