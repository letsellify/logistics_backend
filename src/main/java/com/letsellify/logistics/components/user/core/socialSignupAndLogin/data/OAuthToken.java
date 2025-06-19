package com.letsellify.logistics.components.user.core.socialSignupAndLogin.data;

import com.letsellify.logistics.components.user.core.authorizationToken.data.LogisticsAppSecurityToken;
import com.letsellify.logistics.components.user.core.socialSignupAndLogin.dataMapper.OAuthTokenDataMapper;
import com.letsellify.logistics.components.user.core.socialSignupAndLogin.rest.resource.OAuthTokenResource;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:6/19/25
 * Time:21:57
 */

@Getter
public class OAuthToken {
    private final String issuer;
    private final String accessToken;
    private final String refreshToken;

    public OAuthToken(final LogisticsAppSecurityToken logisticsAppSecurityToken) {
        this.issuer = logisticsAppSecurityToken.getIssuer();
        this.accessToken = logisticsAppSecurityToken.getAccessToken();
        this.refreshToken = logisticsAppSecurityToken.getRefreshToken();
    }


    public OAuthTokenResource getResource() {
        return OAuthTokenDataMapper.INSTANCE.toResource(this);
    }

}
