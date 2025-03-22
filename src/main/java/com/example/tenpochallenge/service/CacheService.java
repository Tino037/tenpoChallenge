package com.example.tenpochallenge.service;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class CacheService {

    private static final String PERCENTAGE_KEY = "percentage";
    private static final Duration CACHE_TTL = Duration.ofMinutes(30);

    private final ReactiveRedisTemplate<String, Double> redisTemplate;

    public CacheService(ReactiveRedisTemplate<String, Double> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<Double> getPercentage() {
        return redisTemplate.opsForValue().get(PERCENTAGE_KEY);
    }

    public Mono<Boolean> setPercentage(Double percentage) {
        return redisTemplate.opsForValue()
                .set(PERCENTAGE_KEY, percentage, CACHE_TTL);
    }

    public Mono<Boolean> clearCache() {
        return redisTemplate.delete(PERCENTAGE_KEY)
                .map(deletedKeys -> deletedKeys > 0);
    }
} 