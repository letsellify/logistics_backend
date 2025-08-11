package com.letsellify.logistics.components.user.core.userManagement.data;

import java.time.Instant;
import java.util.UUID;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.user.core.userManagement.dataMapper.UserDataMapper;
import com.letsellify.logistics.components.user.core.userManagement.database.entity.UserEntity;
import com.letsellify.logistics.components.user.core.userManagement.rest.resource.UserResource;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Getter
public class LogisticsAppUser {
    private final UserEntity entity;


    private final UUID id;

    private final String name;

    private final String email;

    private final String password;

    private final LogisticAppRole role;

    private final boolean active;

    private final Instant creationDate;

    private final Instant lastModifiedDate;

    private final String createdBy;

    private final String lastModifiedBy;

    public LogisticsAppUser(final UserEntity entity) {
        this.entity = entity;
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.password = entity.getPassword();
        this.role = entity.getRole();
        this.active = entity.isActive();
        this.creationDate = entity.getCreationDate();
        this.lastModifiedDate = entity.getLastModifiedDate();
        this.createdBy = entity.getCreatedBy();
        this.lastModifiedBy = entity.getLastModifiedBy();
    }

    public UserResource getResource() {
        return UserDataMapper.INSTANCE.entityToResource(this.entity);
    }

}
