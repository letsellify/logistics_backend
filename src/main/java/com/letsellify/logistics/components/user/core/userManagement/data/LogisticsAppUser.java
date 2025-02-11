package com.letsellify.logistics.components.user.core.userManagement.data;

import java.time.Instant;
import java.util.UUID;

import com.letsellify.logistics.common.data.LogisticsAppRole;
import com.letsellify.logistics.components.user.core.userManagement.database.entity.UserEntity;
import com.letsellify.logistics.components.user.core.userManagement.dataMapper.UserDataMapper;
import com.letsellify.logistics.components.user.core.userManagement.rest.resource.UserResource;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Data
public class LogisticsAppUser {
    private UserEntity entity;

    @NotNull
    private UUID id;
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private LogisticsAppRole role;
    @NotNull
    private boolean active;
    @NotNull
    private boolean isKycPerformed;
    @NotNull
    private Instant creationDate;
    @NotNull
    private Instant lastModifiedDate;
    @NotNull
    private String createdBy;
    @NotNull
    private String lastModifiedBy;

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
    }

    public UserResource getResource() {
        return UserDataMapper.INSTANCE.entityToResource(this.entity);
    }

}
