package com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.repository;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    Page<DispatcherEntity> findByProfileCompleteTrueAndApproveFalse(Pageable pageable);

    @Query("""
        SELECT d FROM DispatcherEntity d
            WHERE d.profileComplete = true
                AND d.approve = true
                    AND d.currentlyAcceptingDelivery = true
                AND (
                        d.receiveAllNotifications = true
                            OR EXISTS (
                                    SELECT p FROM LgaPreferenceEntity p
                                        WHERE p.dispatcher = d
                                            AND p.pickUpLga = :pickup
                                                AND p.dropOffLga = :dropOff
                                )
                    )
    """)
    List<DispatcherEntity> findMatchingDispatchers(@Param("pickup") String pickUpLga, @Param("dropOff") String dropOffLga);

}
