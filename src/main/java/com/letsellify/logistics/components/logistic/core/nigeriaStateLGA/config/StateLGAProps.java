package com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author AHMAD BUBA
 * Date:1/17/25
 * Time:11:56
 */

@ConfigurationProperties(prefix = "state-lga")
public record StateLGAProps(String jsonFilePath) {}
