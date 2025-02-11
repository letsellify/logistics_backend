package com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.database.entity.StateEntity;

/**
 * @author AHMAD BUBA
 * Date:1/17/25
 * Time:11:38
 */

@Repository
public interface StateRepository extends JpaRepository<StateEntity, Long> {
    Optional<StateEntity> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
