package com.letsellify.logistics.common.util;


import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.common.restException.LogisticsRestException;
import com.letsellify.logistics.common.security.data.LogisticsAppSecurityUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Author: Ahmad Buba
 * Date: 8/8/25
 */

public class SecurityUserRoleExtractorHandler {
    public static LogisticAppRole extractUserRole(Authentication authentication) {
        LogisticsAppSecurityUser securityUser = (LogisticsAppSecurityUser) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();

        if (authorities.isEmpty()) {
            throw new LogisticsRestException(HttpStatus.FORBIDDEN, "No role found for user");
        }

        String roleClaim = authorities.iterator().next().getAuthority(); // e.g., "ROLE_VENDOR"
        String enumName = roleClaim.replace("ROLE_", "");

        LogisticAppRole role;
        try {
            role = LogisticAppRole.valueOf(enumName);
        } catch (IllegalArgumentException e) {
            throw new LogisticsRestException(HttpStatus.FORBIDDEN, "Invalid role: " + roleClaim);
        }
        return role;
    }

}
