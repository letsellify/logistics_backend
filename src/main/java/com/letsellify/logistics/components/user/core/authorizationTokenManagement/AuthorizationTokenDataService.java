package com.letsellify.logistics.components.user.core.authorizationTokenManagement;

import java.util.Objects;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.letsellify.logistics.components.user.core.authorizationTokenManagement.rest.resource.TokenResource;

import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Service
@RequiredArgsConstructor
public class AuthorizationTokenDataService {
    private final ObjectProvider<AuthorizationTokenManager> tokenManagerProvider;

    public TokenResource getToken(final Authentication authentication) {
        return Objects.requireNonNull(this.tokenManagerProvider.getIfAvailable())
                      .getToken(authentication)
                      .toTokenResource();
    }

}
