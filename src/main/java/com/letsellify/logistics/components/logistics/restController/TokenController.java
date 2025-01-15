package com.letsellify.logistics.components.logistics.restController;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letsellify.logistics.components.logistics.core.authorizationToken.TokenDataService;
import com.letsellify.logistics.components.logistics.core.authorizationToken.rest.dto.LoginDto;
import com.letsellify.logistics.components.logistics.core.authorizationToken.rest.dto.NewTokenDto;
import com.letsellify.logistics.components.logistics.core.authorizationToken.rest.resource.TokenResource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@RestController
@RequestMapping("/api/v1/authorize")
@Tag(name = "Authorization API", description = "API's for obtaining authorization tokens")
public class TokenController {
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtAuthenticationProvider refreshTokenAuthProvider;
    private final TokenDataService tokenDataService;

    public TokenController(final DaoAuthenticationProvider daoAuthenticationProvider, @Qualifier("jwtRefreshTokenAuthProvider") final JwtAuthenticationProvider refreshTokenAuthProvider, final TokenDataService tokenDataService) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.refreshTokenAuthProvider = refreshTokenAuthProvider;
        this.tokenDataService = tokenDataService;
    }

    @Operation(
      description = "Login a user",
      summary = "Validates user credentials and returns authorization tokens"
    )
    @PostMapping("/login")
    public TokenResource login(@Valid @RequestBody final LoginDto loginDto) {
        final Authentication authentication = this.daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getEmail(), loginDto.getPassword()));
        return this.tokenDataService.getToken(authentication);
    }

    @Operation(
      description = "Get new access token",
      summary = "Using refresh token, obtains new set of authorization tokens"
    )
    @PostMapping("/token")
    public TokenResource getNewTokens(@Valid @RequestBody final NewTokenDto newTokenDto) {
        final Authentication authentication = this.refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(newTokenDto.getRefreshToken()));
        final Jwt jwt = (Jwt) authentication.getCredentials();
        // check if present in db and not revoked, etc

        return this.tokenDataService.getToken(authentication);
    }
}
