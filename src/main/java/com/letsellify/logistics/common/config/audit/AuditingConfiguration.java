package com.letsellify.logistics.common.config.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditingConfiguration {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new LogisticsAuditor();
    }
}
