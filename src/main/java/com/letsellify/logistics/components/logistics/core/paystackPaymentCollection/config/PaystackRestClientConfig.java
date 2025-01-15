package com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:16:39
 */

@Configuration
@EnableConfigurationProperties(PaystackConfig.class)
@RequiredArgsConstructor
public class PaystackRestClientConfig {
    private final PaystackConfig config;

    @Bean
    @Qualifier("PaystackRestClient")
    public RestClient payStackRestClient() {
        return RestClient.builder()
                         .defaultHeader("Authorization", "Bearer " + " ")
                         .defaultHeader("Content-Type", "application/json")
                         .baseUrl("https://api.paystack.com/")
                         .build();
    }
}
