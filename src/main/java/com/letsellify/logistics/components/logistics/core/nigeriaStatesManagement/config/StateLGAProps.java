package com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author AHMAD BUBA
 * Date:1/17/25
 * Time:11:56
 */

@ConfigurationProperties(prefix = "state-lga")
public record StateLGAProps(String jsonFilePath) {
}
