package com.letsellify.logistics.components.user.core.socialSignupAndLoginManagement;

import org.springframework.stereotype.Service;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import com.letsellify.logistics.components.user.core.socialSignupAndLoginManagement.rest.resource.OAuthTokenResource;
import com.letsellify.logistics.common.util.CookieHandler;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:6/12/25
 * Time:13:32
 */

@Service
@RequiredArgsConstructor
public class SocialLoginAndSignUpDataService {
    private final SocialSignupAndLoginManager socialSignupAndLoginManager;
    private static final String COOKIE_DOMAIN =  "localhost";

    public void assignRole(final HttpServletResponse httpServletResponse, final @NonNull String token, final @NonNull LogisticAppRole role) {
        final OAuthTokenResource oAuthTokenResource;
        try {
            oAuthTokenResource = this.socialSignupAndLoginManager.assignRole(token, role).getResource();
        }
        catch (final UserNotFoundException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
        CookieHandler.addCookie(httpServletResponse, COOKIE_DOMAIN,"access_token", oAuthTokenResource.accessToken(), 15 * 60);
        CookieHandler.addCookie(httpServletResponse, COOKIE_DOMAIN, "refresh_token", oAuthTokenResource.refreshToken(),7 * 24 * 60 * 60);
    }

}
