package com.letsellify.logistics.components.user.restController;

import com.letsellify.logistics.common.util.CookieHandler;
import com.letsellify.logistics.components.user.core.authorizationTokenManagement.AuthorizationTokenDataService;
import com.letsellify.logistics.components.user.core.authorizationTokenManagement.rest.dto.LoginDto;
import com.letsellify.logistics.components.user.core.authorizationTokenManagement.rest.resource.TokenResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@RestController
@RequestMapping("/api/v1/authorize")
@Tag(
        name = "Authorization API",
        description = "API endpoints for authenticating users and obtaining JWT authorization tokens"
)
@Slf4j
public class TokenController {

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtAuthenticationProvider refreshTokenAuthProvider;
    private final AuthorizationTokenDataService tokenDataService;

    public TokenController(final DaoAuthenticationProvider daoAuthenticationProvider, @Qualifier("jwtRefreshTokenAuthProvider") final JwtAuthenticationProvider refreshTokenAuthProvider, final AuthorizationTokenDataService tokenDataService) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.refreshTokenAuthProvider = refreshTokenAuthProvider;
        this.tokenDataService = tokenDataService;
    }

    @Operation(
            summary = "Login a user",
            description = """
                        Validates user credentials and returns authorization tokens.
                    
                        **Behavior:**
                        - For **web clients** (`X-Client-Type: web`):  
                          Tokens are returned as `HttpOnly` cookies (`access_token` and `refresh_token`).  
                          Requires the request to come from a recognized domain.
                        - For **mobile clients** (`X-Client-Type: mobile`):  
                          Tokens are returned in the JSON response body.
                    
                        **Client Type Rules:**  
                        The `X-Client-Type` header must be either `mobile` or `web`.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "User login credentials",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoginDto.class)
                    )
            ),
            parameters = {
                    @Parameter(name = "X-Client-Type", description = "Specifies client type (mobile or web)", required = true, example = "mobile")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful. Tokens issued."),
                    @ApiResponse(responseCode = "401", description = "Invalid client type or credentials"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(
            final HttpServletRequest httpServletRequest,
            @RequestHeader("X-Client-Type") final String clientType,
            @RequestBody final @Valid LoginDto loginDto,
            final HttpServletResponse httpServletResponse
    ) {
        if (clientType == null || clientType.isEmpty() || (!clientType.equalsIgnoreCase("mobile") && !clientType.equalsIgnoreCase("web"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid client type");
        }
        final Authentication authentication = this.daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getEmail(), loginDto.getPassword()));
        final TokenResource tokenResource = this.tokenDataService.getToken(authentication);
        if ("web".equalsIgnoreCase(clientType)) {
            final String serverName = httpServletRequest.getServerName();
            final String domain;
            if (serverName.contains("localhost")) {
                domain = "localhost";
            } else if (serverName.contains(".letsellify")) {
                domain = "logistics.letsellify.com";
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Origin header not detected");
            }
            log.info("Request comes from: {}", domain);
            CookieHandler.addCookie(httpServletResponse, domain, "access_token", tokenResource.getAccessToken(), 15 * 60);
            CookieHandler.addCookie(httpServletResponse, domain, "refresh_token", tokenResource.getRefreshToken(), 7 * 24 * 60 * 60);
            return ResponseEntity.ok(Map.of("message", "Login Successful"));
        }

        return ResponseEntity.ok(Map.of(
                "issuer", tokenResource.getIssuer(),
                "access_token", tokenResource.getAccessToken(),
                "refresh_token", tokenResource.getRefreshToken()
        ));

    }


    @Operation(
            summary = "Refresh access token",
            description = """
                        Obtains a new set of access and refresh tokens using an existing refresh token.
                    
                        **Behavior:**
                        - **Mobile clients** must provide `refresh_token` in the request body.
                        - **Web clients** must have `refresh_token` in an `HttpOnly` cookie.
                    
                        **Client Type Rules:**  
                        The `X-Client-Type` header must be either `mobile` or `web`.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = false,
                    description = "Refresh token (mobile clients only)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    example = """
                                            {
                                              "refresh_token": "your-refresh-token"
                                            }
                                            """
                            )
                    )
            ),
            parameters = {
                    @Parameter(name = "X-Client-Type", description = "Specifies client type (mobile or web)", required = true, example = "mobile"),
                    @Parameter(name = "Origin", description = "Origin header (web clients only)", required = false),
                    @Parameter(name = "refresh_token", in = ParameterIn.COOKIE, description = "Refresh token (web clients only)", required = false)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tokens successfully refreshed"),
                    @ApiResponse(responseCode = "401", description = "Invalid client type or missing/invalid refresh token"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            }
    )
    @PostMapping("/token")
    public ResponseEntity<?> getNewTokens(
            final HttpServletRequest httpServletRequest,
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
        } else {
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
            final String serverName = httpServletRequest.getServerName();
            final String domain;
            if (serverName.contains("localhost")) {
                domain = "localhost";
            } else if (serverName.contains(".letsellify")) {
                domain = "logistics.letsellify.com";
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Origin header not detected");
            }
            CookieHandler.addCookie(httpServletResponse, domain, "access_token", tokenResource.getAccessToken(), 15 * 60);
            CookieHandler.addCookie(httpServletResponse, domain, "refresh_token", tokenResource.getRefreshToken(), 7 * 24 * 60 * 60);
            return ResponseEntity.ok(Map.of("message", "Token successfully refreshed"));

        }
        return ResponseEntity.ok(Map.of(
                "issuer", tokenResource.getIssuer(),
                "access_token", tokenResource.getAccessToken(),
                "refresh_token", tokenResource.getRefreshToken()
        ));
    }


    @Operation(
            summary = "Logout a user",
            description = """
                        Logs out the authenticated user by clearing issued cookies.

                        **Behavior:**
                        - For **web clients**:  
                          Clears `access_token` and `refresh_token` cookies on the domain.  
                          Effectively invalidates the session on the browser side.
                        - For **mobile clients**:  
                          Since tokens are returned in the response body and stored locally, the client
                          must manually delete any stored tokens.
                          
                        **Important:**  
                        This endpoint does not revoke tokens server-side (if token revocation is required,
                        implement it at the token store or database level). It only clears cookies on the client.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logout successful. Cookies cleared."),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized or invalid client")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) {
        String domain = httpServletRequest.getServerName();
        if ("localhost".equals(domain)) {
            domain = "localhost";
        }
        else
            domain = "logistics.letsellify.com";
        CookieHandler.clearCookie(httpServletResponse, domain, "access_token");
        CookieHandler.clearCookie(httpServletResponse, domain, "refresh_token");

        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }


}
