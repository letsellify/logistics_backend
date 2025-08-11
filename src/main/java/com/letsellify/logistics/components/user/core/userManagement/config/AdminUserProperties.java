package com.letsellify.logistics.components.user.core.userManagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author AHMAD BUBA
 * Date:2/22/25
 * Time:16:41
 */

@ConfigurationProperties(prefix = "logistics.admin")
public record AdminUserProperties(String name, String email, String password) {}
