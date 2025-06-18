package com.letsellify.logistics.common.security.config;

import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author AHMAD BUBA
 * Date:6/12/25
 * Time:14:27
 */

@Component
public class CookieBearerTokenResolver implements BearerTokenResolver {

    @Override
    public String resolve(final HttpServletRequest request) {
        /* Look for the token in the Authorization header first */
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        /* If not found, look in cookies */
        if (request.getCookies() != null) {
            for (final Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
