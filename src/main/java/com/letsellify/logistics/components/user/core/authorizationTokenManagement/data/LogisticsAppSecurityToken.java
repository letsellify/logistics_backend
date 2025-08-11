package com.letsellify.logistics.components.user.core.authorizationTokenManagement.data;

import com.letsellify.logistics.components.user.core.authorizationTokenManagement.dataMapper.TokenDataMapper;
import com.letsellify.logistics.components.user.core.authorizationTokenManagement.rest.resource.TokenResource;

import lombok.Getter;
import lombok.NonNull;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Getter
public class LogisticsAppSecurityToken {
    private final String issuer;
    private final String accessToken;
    private String refreshToken;

    public LogisticsAppSecurityToken(final @NonNull String issuer, final @NonNull String accessToken) {
        this.issuer = issuer;
        this.accessToken = accessToken;
    }

    public LogisticsAppSecurityToken(final @NonNull String issuer, final @NonNull String accessToken, final @NonNull String refreshToken) {
        this.issuer = issuer;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void addRefreshToken(final @NonNull String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TokenResource toTokenResource() {
        return TokenDataMapper.INSTANCE.dataToResource(this);
    }

}
