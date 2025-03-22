package com.letsellify.logistics.components.user.core.authorizationToken.rest.resource;

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
public class TokenResource {
    private final String issuer;

    private final String accessToken;

    private final String refreshToken;
}
