package com.letsellify.logistics.components.user.core.socialSignupAndLoginManagement;

import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.common.security.data.LogisticsAppSecurityUser;
import com.letsellify.logistics.components.user.core.authorizationTokenManagement.AuthorizationTokenManager;
import com.letsellify.logistics.components.user.core.authorizationTokenManagement.data.LogisticsAppSecurityToken;
import com.letsellify.logistics.components.user.core.userManagement.UserManager;
import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUser;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import com.letsellify.logistics.components.user.core.socialSignupAndLoginManagement.data.OAuthToken;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:6/12/25
 * Time:13:32
 */

@Component
@RequiredArgsConstructor
public class SocialSignupAndLoginManager {
    private final UserManager userManager;
    private final AuthorizationTokenManager tokenManager;

    OAuthToken assignRole(final @NonNull String token, final @NonNull LogisticAppRole role) throws JwtException, UserNotFoundException {
        final String username = this.tokenManager.getUsernameFromToken(token);
        final LogisticsAppUser appUser = this.userManager.assignRoleForAuthUser(username, role);
        final LogisticsAppSecurityToken securityToken = this.tokenManager.getToken(new LogisticsAppSecurityUser(appUser));
        return new OAuthToken(securityToken);
    }
}
