package com.letsellify.logistics.components.logistics.core.agentManagement.database.repository;

import com.letsellify.logistics.components.logistics.core.agentManagement.database.entity.AgentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:2/24/25
 * Time:21:17
 */

@Repository
public interface AgentRepository extends JpaRepository<AgentEntity, UUID> {
    Optional<AgentEntity> findByEmail(String email);

    List<AgentEntity> findByApprovedAndAcceptingStorage(boolean approved, boolean acceptingStorage);

    boolean existsByEmail(String agentEmail);

    Page<AgentEntity> findByApprovedFalseAndProfileCompleteTrue(Pageable pageable);

}
