package com.letsellify.logistics.components.user.core.authorizationTokenManagement;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.letsellify.logistics.components.user.core.authorizationTokenManagement.data.LogisticsAppSecurityToken;
import com.letsellify.logistics.common.security.data.LogisticsAppSecurityUser;

import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Component
@Slf4j
public class AuthorizationTokenManager {
    private final JwtEncoder accessTokenEncoder;

    private final JwtEncoder refreshTokenEncoder;

    private final JwtDecoder jwtDecoder;

    private static final String ISSUER = "Letsellify Logistics";


    public AuthorizationTokenManager(final JwtEncoder accessTokenEncoder, @Qualifier("jwtRefreshTokenEncoder") final JwtEncoder refreshTokenEncoder, final JwtDecoder jwtDecoder) {
        this.accessTokenEncoder = accessTokenEncoder;
        this.refreshTokenEncoder = refreshTokenEncoder;
        this.jwtDecoder = jwtDecoder;
    }


    public LogisticsAppSecurityToken getToken(final Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof final LogisticsAppSecurityUser securityUser)) {
            throw new BadCredentialsException(
              MessageFormat.format("principal {0} is not of User type", authentication.getPrincipal().getClass())
            );
        }

        final String accessToken = this.createAccessToken(authentication);
        final LogisticsAppSecurityToken securityToken = new LogisticsAppSecurityToken(ISSUER, accessToken);

        final String refreshToken;
        if (authentication.getCredentials() instanceof final Jwt jwt) {
            final Instant now = Instant.now();
            final Instant expiresAt = jwt.getExpiresAt();
            final Duration duration = Duration.between(now, expiresAt);
            final long daysUntilExpired = duration.toDays();
            if (daysUntilExpired < 7) {
                refreshToken = this.createRefreshToken(authentication);
            } else {
                refreshToken = jwt.getTokenValue();
            }
        } else {
            refreshToken = this.createRefreshToken(authentication);
        }
        securityToken.addRefreshToken(refreshToken);

        return securityToken;
    }

    public LogisticsAppSecurityToken getToken(final LogisticsAppSecurityUser securityUser) {
        return new LogisticsAppSecurityToken(ISSUER, this.buildAccessToken(securityUser), this.buildRefreshToken(securityUser));
    }

    public String getAccessTokenForOAuth2(final LogisticsAppSecurityUser securityUser) {
        return this.buildAccessTokenForOAuth2(securityUser);
    }

    public String getUsernameFromToken(final String token) throws JwtException {
        final Jwt jwt = this.jwtDecoder.decode(token);
        return jwt.getSubject();
    }


    private String createAccessToken(final Authentication authentication) {
        final LogisticsAppSecurityUser securityUser = (LogisticsAppSecurityUser) authentication.getPrincipal();
        return this.buildAccessToken(securityUser);
    }

    private String createRefreshToken(final Authentication authentication) {
        final LogisticsAppSecurityUser securityUser = (LogisticsAppSecurityUser) authentication.getPrincipal();
        return this.buildRefreshToken(securityUser);
    }


    private String buildAccessToken(final LogisticsAppSecurityUser securityUser) {
        final String role = securityUser.getAuthorities().stream()
                                                  .findFirst()
                                                  .map(GrantedAuthority::getAuthority)
                                                  .orElse(null);

        final Instant now = Instant.now();

        final JwtClaimsSet.Builder claimsSet = JwtClaimsSet.builder()
                                                               .issuer(ISSUER)
                                                               .issuedAt(now)
                                                               .expiresAt(now.plus(1, ChronoUnit.DAYS))
                                                               .subject(securityUser.getUsername())
                                                               .claim("token_type", "access");


        if (role != null) {
            claimsSet.claim("role", role);
        }
        return this.accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet.build())).getTokenValue();
    }

    private String buildAccessTokenForOAuth2(final LogisticsAppSecurityUser securityUser) {
        final String role = securityUser.getAuthorities().stream()
                                        .findFirst()
                                        .map(GrantedAuthority::getAuthority)
                                        .orElse(null);

        final Instant now = Instant.now();

        final JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                                                   .issuer(ISSUER)
                                                   .issuedAt(now)
                                                   .expiresAt(now.plus(10, ChronoUnit.MINUTES))
                                                   .subject(securityUser.getUsername())
                                                   .claim("token_type", "access")
                                                   .build();


        return this.accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    private String buildRefreshToken(final LogisticsAppSecurityUser securityUser) {
        final Instant now = Instant.now();

        final JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                                                   .issuer(ISSUER)
                                                   .issuedAt(now)
                                                   .expiresAt(now.plus(30, ChronoUnit.DAYS))
                                                   .subject(securityUser.getUsername())
                                                   .claim("token_type", "refresh")
                                                   .build();

        return this.refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

}
