package com.letsellify.logistics.components.user.core.securityManagement.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.letsellify.logistics.components.user.core.securityManagement.config.keys.AccessTokenRsaKey;
import com.letsellify.logistics.components.user.core.securityManagement.config.keys.RefreshTokenRsaKey;
import com.letsellify.logistics.components.user.core.securityManagement.data.LogisticsAppSecurityUser;
import com.letsellify.logistics.components.user.core.userManagement.UserManager;
import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUser;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Configuration
@EnableConfigurationProperties({ AccessTokenRsaKey.class, RefreshTokenRsaKey.class})
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
// @PreAuthorize @PostAuthorize @PreFilter @PostFilter
public class SecurityConfiguration {
    private final AccessTokenRsaKey accessTokenRsaKey;
    private final RefreshTokenRsaKey refreshTokenRsaKey;
    private final JwtTokenToAuthentication jwtTokenToAuthentication;
    private final UserManager userManager;


    private static final String[] AUTH_WHITELIST = {
            "/api/v1/authorize/**",
            "/api/v1/user-management/register",
            "/api/v1/verification/**",
            "/api/v1/stateLGA/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/actuator/**",
            "/oauth2/**",
            "/error",
            "/favicon.ico",
            "/*.png",
            "/*.gif",
            "/*.svg",
            "/*.jpg",
            "/*.html",
            "/*.css",
            "/*.js"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(AUTH_WHITELIST)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer((oauth2) ->
                        oauth2.jwt((jwt) -> jwt.jwtAuthenticationConverter(this.jwtTokenToAuthentication))
                )
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService()  {
        return username -> {
            final LogisticsAppUser user;
            try {
                user = SecurityConfiguration.this.userManager.getUserByEmail(username);
            }
            catch (final UserNotFoundException e) {
                throw new AuthenticationCredentialsNotFoundException(e.getMessage());
            }
            return new LogisticsAppSecurityUser(user);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(this.passwordEncoder());
        provider.setUserDetailsService(this.userDetailsService());
        return provider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://127.0.0.1:4200", "http://letsellify.com", "https://letsellify.com", "http://api.letsellify.com", "https://api.letsellify.com"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Primary
    JwtDecoder jwtAccessTokenDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.accessTokenRsaKey.publicKey()).build();
    }

    @Bean
    @Primary
    JwtEncoder jwtAccessTokenEncoder() {
        final JWK jwk = new RSAKey.Builder(this.accessTokenRsaKey.publicKey()).privateKey(this.accessTokenRsaKey.privateKey()).build();
        final JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }


    @Bean
    @Qualifier("jwtRefreshTokenDecoder")
    JwtDecoder jwtRefreshTokenDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.refreshTokenRsaKey.publicKey()).build();
    }

    @Bean
    @Qualifier("jwtRefreshTokenEncoder")
    JwtEncoder jwtRefreshTokenEncoder() {
        final JWK jwk = new RSAKey
                .Builder(this.refreshTokenRsaKey.publicKey())
                .privateKey(this.refreshTokenRsaKey.privateKey())
                .build();
        final JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    @Qualifier("jwtRefreshTokenAuthProvider")
    JwtAuthenticationProvider jwtRefreshTokenAuthProvider() {
        final JwtAuthenticationProvider provider = new JwtAuthenticationProvider(this.jwtRefreshTokenDecoder());
        provider.setJwtAuthenticationConverter(this.jwtTokenToAuthentication);
        return provider;
    }


}
