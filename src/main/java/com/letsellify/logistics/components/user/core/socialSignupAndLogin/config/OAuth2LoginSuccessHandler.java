package com.letsellify.logistics.components.user.core.socialSignupAndLogin.config;

import java.io.IOException;
import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsellify.logistics.common.security.data.LogisticsAppSecurityUser;
import com.letsellify.logistics.components.user.core.authorizationToken.AuthorizationTokenManager;
import com.letsellify.logistics.components.user.core.authorizationToken.data.LogisticsAppSecurityToken;
import com.letsellify.logistics.components.user.core.logisticUser.UserManager;
import com.letsellify.logistics.components.user.core.logisticUser.data.LogisticsAppUser;
import com.letsellify.logistics.components.user.core.logisticUser.exception.UserExistsException;
import com.letsellify.logistics.components.user.core.logisticUser.exception.UserNotFoundException;
import com.letsellify.logistics.components.user.core.socialSignupAndLogin.data.LogisticOAuth2User;
import com.letsellify.logistics.components.user.util.CookieUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * @author AHMAD BUBA
 * Date:2/14/25
 * Time:15:30
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserManager userManager;
    private final AuthorizationTokenManager tokenManager;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        final OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        final String email = (String) oauthUser.getAttribute("email");
        final OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        final String provider = oauthToken.getAuthorizedClientRegistrationId();
        LogisticsAppUser appUser;
        try {
            appUser = this.userManager.getUserByEmail(Objects.requireNonNull(email));
        } catch (final UserNotFoundException e) {
            try {
                final LogisticOAuth2User oAuth2User = new LogisticOAuth2User(oauthUser, provider);
                appUser = this.userManager.processOAuth2User(oAuth2User);
            } catch (final UserExistsException ex) {
                log.error("User exists " +  ex.getMessage());
                return;
            }
        }

        final LogisticsAppSecurityUser securityUser = new LogisticsAppSecurityUser(appUser);

//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        this.addSecurityHeaders(response);

        if (appUser.getRole() == null) {
            final String token = this.tokenManager.getAccessTokenForOAuth2(securityUser);
            response.sendRedirect("https://logistics.letsellify/join-as/SIGNUP?" + token);
        } else {
            final String domain = ".letsellify";
            final LogisticsAppSecurityToken securityToken = this.tokenManager.getToken(securityUser);
            CookieUtil.addCookie(response, domain, "access_token", securityToken.getAccessToken(), 15 * 60);
            CookieUtil.addCookie(response, domain,"refresh_token", securityToken.getRefreshToken(), 7 * 24 * 60 * 60);
            this.addSecurityHeaders(response);
            response.sendRedirect("https://logistics.letsellify/homepage");
        }
    }

    private void addSecurityHeaders(final HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
    }

    private void writeErrorResponse(final HttpServletResponse response, final String message) throws IOException {
        response.setStatus(400);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        log.warn("Error Response: {} - {}", 400, message);
    }
}
