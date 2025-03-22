package com.letsellify.logistics.components.logistic.core.request.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.letsellify.logistics.components.logistic.core.request.database.entity.LogisticRequestEntity;

/**
 * @author AHMAD BUBA
 * Date:2/21/25
 * Time:17:10
 */

@Configuration
@EnableCaching
public class RedisConfiguration {
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(); // Lettuce is preferred over Jedis for better performance
    }

    @Bean
    public RedisTemplate<String, LogisticRequestEntity> logisticRequestRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, LogisticRequestEntity> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
