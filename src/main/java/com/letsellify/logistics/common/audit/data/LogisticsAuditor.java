package com.letsellify.logistics.common.audit.data;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.letsellify.logistics.components.user.core.securityManagement.data.LogisticsAppSecurityUser;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

public class LogisticsAuditor implements AuditorAware<String> {

    private static final String SYSTEM = "system";

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        final Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.of(SYSTEM);
        }

        final LogisticsAppSecurityUser principal = (LogisticsAppSecurityUser) authentication.getPrincipal();
        return Optional.ofNullable(principal.getUsername());
    }
}
