package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author AHMAD BUBA
 * Date:3/13/25
 * Time:21:31
 */

@Configuration
public class AxonConfiguration {
    @Bean
    InitializingBean initializingBean(final ObjectMapper objectMapper) {
        return () -> objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT
        );
    }
}
