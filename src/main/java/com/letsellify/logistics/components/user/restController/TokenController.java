package com.letsellify.logistics.components.user.restController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letsellify.logistics.components.user.core.authorizationToken.AuthorizationTokenDataService;
import com.letsellify.logistics.components.user.core.authorizationToken.rest.dto.LoginDto;
import com.letsellify.logistics.components.user.core.authorizationToken.rest.resource.TokenResource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@RestController
@RequestMapping("/api/v1/authorize")
@Tag(name = "Authorization API",
     description = "API's for obtaining authorization tokens")
public class TokenController {

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtAuthenticationProvider refreshTokenAuthProvider;
    private final AuthorizationTokenDataService tokenDataService;

    public TokenController(final DaoAuthenticationProvider daoAuthenticationProvider, @Qualifier("jwtRefreshTokenAuthProvider") final JwtAuthenticationProvider refreshTokenAuthProvider, final AuthorizationTokenDataService tokenDataService) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.refreshTokenAuthProvider = refreshTokenAuthProvider;
        this.tokenDataService = tokenDataService;
    }

    @Operation(description = "Login a user",
               summary = "Validates user credentials and returns authorization tokens")
    @PostMapping("/login")
    public ResponseEntity<?> login(
      @RequestHeader(value = "Origin", required = false) final String origin,
      @RequestHeader("X-Client-Type") final String clientType,
      @RequestBody final @Valid LoginDto loginDto,
      final HttpServletResponse httpServletResponse
    ) {
        if (clientType == null || clientType.isEmpty() || (!clientType.equalsIgnoreCase("mobile") && !clientType.equalsIgnoreCase("web"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid client type");
        }
        final Authentication authentication = this.daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getEmail(), loginDto.getPassword()));
        final TokenResource tokenResource = this.tokenDataService.getToken(authentication);
        String domain = null;
        if ("web".equalsIgnoreCase(clientType)) {
            if (origin != null && origin.contains("localhost")) {
                domain = "localhost";
            } else if (origin != null && origin.contains(".letsellify")) {
                domain = ".letsellify";
            }

            if (domain != null) {
                addCookie(httpServletResponse, domain, "access_token", tokenResource.getAccessToken(), 15 * 60);
                addCookie(httpServletResponse, domain, "refresh_token", tokenResource.getRefreshToken(), 7 * 24 * 60 * 60);
                return ResponseEntity.ok(Map.of("message", "Login Successful"));
            }
        }

        return ResponseEntity.ok(Map.of(
          "issuer", tokenResource.getIssuer(),
          "access_token", tokenResource.getAccessToken(),
          "refresh_token", tokenResource.getRefreshToken()
        ));
    }


    @Operation(description = "Get new access token",
               summary = "Using refresh token, obtains new set of authorization tokens")
    @PostMapping("/token")
    public ResponseEntity<?> getNewTokens(
      @RequestHeader(value = "Origin", required = false) final String origin,
      @RequestHeader("X-Client-Type") final String clientType,
      @CookieValue(value = "refresh_token", required = false) final String refreshTokenCookie,
      @RequestBody(required = false) final Map<String, String> requestBody,
      final HttpServletResponse httpServletResponse
    ) {
        if (clientType == null || clientType.isEmpty() || (!clientType.equalsIgnoreCase("mobile") && !clientType.equalsIgnoreCase("web"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid client type");
        }
        final String refreshToken;
        if (clientType.equalsIgnoreCase("mobile")) {
            if (requestBody == null || !requestBody.containsKey("refresh_token")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing refresh token");
            }
            refreshToken = requestBody.get("refresh_token");
        }
        else {
            refreshToken = refreshTokenCookie;
        }

        if (refreshToken == null) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing refresh token");
        }
        final Authentication authentication = this.refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(refreshToken));
        final Jwt jwt = (Jwt) authentication.getCredentials();
        // check if present in db and not revoked, etc

        final TokenResource tokenResource = this.tokenDataService.getToken(authentication);

        if ("web".equalsIgnoreCase(clientType)) {
            String domain = null;
            if (origin != null && origin.contains("localhost")) {
                domain = "localhost";
            }
            else if (origin != null && origin.contains(".letsellify")) {
                domain = ".letsellify";
            }
            if (domain == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Origin header not detected");
            }
            this.addCookie(httpServletResponse, domain, "access_token", tokenResource.getAccessToken(), 15*60);
            this.addCookie(httpServletResponse, domain, "refresh_token",tokenResource.getRefreshToken(),7 * 24 * 60 * 60);
            return ResponseEntity.ok(Map.of("message", "Token successfully refreshed"));

        }
        return ResponseEntity.ok(Map.of(
          "issuer", tokenResource.getIssuer(),
          "access_token", tokenResource.getAccessToken(),
          "refresh_token", tokenResource.getRefreshToken()
        ));
    }

    public static void addCookie(final HttpServletResponse response, final String domain, final String name, final String value, final int maxAge) {
        final Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(!domain.equalsIgnoreCase("localhost"));
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

}
