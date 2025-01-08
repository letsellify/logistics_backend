package com.letsellify.logistics.components.user.core.userManagement.rest.resource;

import java.time.Instant;

import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Data
public class UserResource {
    @NotNull
    private String name;
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    private LogisticsAppRole role;
    @NotNull
    private boolean active;
    @NotNull
    private Instant creationDate;
    @NotNull
    private Instant lastModifiedDate;
    @NotNull
    private String createdBy;
    @NotNull
    private String lastModifiedBy;
}
