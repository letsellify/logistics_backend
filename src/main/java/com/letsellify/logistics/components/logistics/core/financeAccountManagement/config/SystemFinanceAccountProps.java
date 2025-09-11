package com.letsellify.logistics.components.logistics.core.financeAccountManagement.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Author: Ahmad Buba
 * Date: 8/25/25
 */


@ConfigurationProperties(prefix = "system.account")
public record SystemFinanceAccountProps(String id) {
}
