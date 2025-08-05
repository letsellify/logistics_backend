package com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:16:36
 */

@ConfigurationProperties(prefix = "paystack")
public record PaystackProps(String baseUrl, String secretKey) {}
