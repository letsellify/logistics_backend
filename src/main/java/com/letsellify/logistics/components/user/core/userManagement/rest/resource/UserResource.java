package com.letsellify.logistics.components.user.core.userManagement.rest.resource;

import com.letsellify.logistics.common.data.LogisticAppRole;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class UserResource {
    private final String name;

    private final String email;

    private final LogisticAppRole role;

    private final boolean active;

}
