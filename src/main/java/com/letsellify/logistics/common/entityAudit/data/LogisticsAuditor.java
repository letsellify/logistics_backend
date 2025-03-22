package com.letsellify.logistics.common.entityAudit.data;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.letsellify.logistics.common.security.data.LogisticsAppSecurityUser;

import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Slf4j
public class LogisticsAuditor implements AuditorAware<String> {

    private static final String SYSTEM = "system";

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
            !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            log.debug("No authenticated user found. Returning SYSTEM.");
            return Optional.of(SYSTEM);
        }

        final Object principal = authentication.getPrincipal();
        if (principal instanceof final LogisticsAppSecurityUser user) {
            final String username = user.getUsername();
            log.debug("Found authenticated user: {}", username);
            return Optional.ofNullable(username);
        }

        log.warn("Unexpected principal type: {}. Returning SYSTEM.", principal.getClass().getName());
        return Optional.of(SYSTEM);
    }
}
