package com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:16:39
 */

@Configuration
@EnableConfigurationProperties(PaystackProps.class)
@RequiredArgsConstructor
public class PaystackRestClientConfig {
    private final PaystackProps paystackProps;

    @Bean
    @Qualifier("PaystackRestClient")
    public RestClient payStackRestClient() {
        return RestClient.builder()
                .defaultHeader("Authorization", "Bearer " + this.paystackProps.secretKey())
                .defaultHeader("Content-Type", "application/json")
                .baseUrl(this.paystackProps.baseUrl())
                .build();
    }
}
