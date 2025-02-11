package com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author AHMAD BUBA
 * Date:1/17/25
 * Time:11:56
 */

@ConfigurationProperties(prefix = "state-lga")
public record StateLGAJsonFilePathConfig(String jsonFilePath) {}
