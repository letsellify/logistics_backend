package com.letsellify.logistics.components.logistic.core.request.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author AHMAD BUBA
 * Date:3/13/25
 * Time:21:31
 */

@Configuration
public class AxonConfiguration {
    @Bean
    InitializingBean initializingBean(ObjectMapper objectMapper) {
        return () -> objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                                                         ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT
        );
    }
}
