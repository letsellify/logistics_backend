package com.letsellify.logistics.components.user.core.authorizationTokenManagement.data;

import com.letsellify.logistics.components.user.core.authorizationTokenManagement.mapper.TokenDataMapper;
import com.letsellify.logistics.components.user.core.authorizationTokenManagement.rest.resource.TokenResource;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Data
public class LogisticsAppSecurityToken {
    private String issuer;
    private String accessToken;
    private String refreshToken;

    public TokenResource toTokenResource() {
        return TokenDataMapper.INSTANCE.dataToResource(this);
    }
}
