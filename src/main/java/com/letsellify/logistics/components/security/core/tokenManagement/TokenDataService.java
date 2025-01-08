package com.letsellify.logistics.components.security.core.tokenManagement;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.letsellify.logistics.components.security.core.tokenManagement.rest.resource.TokenResource;

import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Service
@RequiredArgsConstructor
public class TokenDataService {
    private final TokenManager tokenManager;

    public TokenResource getToken(final Authentication authentication) {
        return this.tokenManager.getToken(authentication)
                                   .toTokenResource();
    }

}
