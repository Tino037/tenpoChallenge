package com.example.tenpochallenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, Double> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Double> valueSerializer = new Jackson2JsonRedisSerializer<>(Double.class);
        
        RedisSerializationContextBuilder<String, Double> builder = RedisSerializationContext.newSerializationContext(keySerializer);
        
        RedisSerializationContext<String, Double> context = builder
                .value(valueSerializer)
                .build();
        
        return new ReactiveRedisTemplate<>(factory, context);
    }
} 