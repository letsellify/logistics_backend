package com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.repository;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:04:26
 */

@Repository
public interface DispatcherRepository extends JpaRepository<DispatcherEntity, UUID> {
    Optional<DispatcherEntity> findByEmail(String email);

    List<DispatcherEntity> findByCurrentlyAcceptingDeliveryAndApprove(boolean currentlyAcceptingDelivery, boolean approve);

    boolean existsByEmail(String dispatcherEmail);

}
