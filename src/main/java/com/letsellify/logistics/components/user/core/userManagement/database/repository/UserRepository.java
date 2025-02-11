package com.letsellify.logistics.components.user.core.userManagement.database.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.letsellify.logistics.components.user.core.userManagement.database.entity.UserEntity;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
}
