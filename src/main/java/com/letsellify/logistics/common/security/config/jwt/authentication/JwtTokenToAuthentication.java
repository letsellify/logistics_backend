package com.letsellify.logistics.common.security.config.jwt.authentication;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.letsellify.logistics.common.security.data.LogisticsAppSecurityUser;
import com.letsellify.logistics.components.user.core.userManagement.UserManager;
import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUser;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Service
@RequiredArgsConstructor
public class JwtTokenToAuthentication implements Converter<Jwt, UsernamePasswordAuthenticationToken> {
    private final UserManager userManager;

    @Override
    public UsernamePasswordAuthenticationToken convert(final Jwt source) {
        final String username = source.getSubject();
        final LogisticsAppSecurityUser securityUser;
        final LogisticsAppUser appUser;
        try {
            appUser = this.userManager.getUserByEmail(username);
        }
        catch (final UserNotFoundException e) {
            throw new AuthenticationCredentialsNotFoundException(e.getMessage());
        }
        securityUser = new LogisticsAppSecurityUser(appUser);
        return new UsernamePasswordAuthenticationToken(securityUser, source, securityUser.getAuthorities());
    }
}
