package com.letsellify.logistics.common.security.config;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.letsellify.logistics.common.security.data.LogisticsAppSecurityUser;
import com.letsellify.logistics.components.user.core.logisticUser.UserManager;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Service
@RequiredArgsConstructor
public class JwtTokenToAuthentication implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    private final UserManager userManager;

    @SneakyThrows
    @Override
    public UsernamePasswordAuthenticationToken convert(final Jwt source) {
        final String username = source.getSubject();
        final String tokenType = source.getClaimAsString("token_type");
        final String role = source.getClaimAsString("role");
        final LogisticsAppSecurityUser securityUser = new LogisticsAppSecurityUser(this.userManager.getUserByEmail(username));

        if ("access".equals(tokenType)) {
            if (role == null) {
                throw new AccessDeniedException("Access denied: No role assigned.");
            }
            final GrantedAuthority authority = new SimpleGrantedAuthority(role);
            return new UsernamePasswordAuthenticationToken(securityUser, null, List.of(authority));
        }

        // For refresh tokens, only validate the user
        if ("refresh".equals(tokenType)) {
            return new UsernamePasswordAuthenticationToken(securityUser, null, List.of());
        }

        throw new AuthenticationCredentialsNotFoundException("Invalid token type");
    }

}
