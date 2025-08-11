package com.letsellify.logistics.components.user.core.socialSignupAndLoginManagement.data;

import com.letsellify.logistics.components.user.core.authorizationTokenManagement.data.LogisticsAppSecurityToken;
import com.letsellify.logistics.components.user.core.socialSignupAndLoginManagement.dataMapper.OAuthTokenDataMapper;
import com.letsellify.logistics.components.user.core.socialSignupAndLoginManagement.rest.resource.OAuthTokenResource;

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
