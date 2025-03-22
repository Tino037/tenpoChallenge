package com.example.tenpochallenge.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class RateLimiterServiceTest {

    private RateLimiterService rateLimiterService;

    @BeforeEach
    void setUp() {
        rateLimiterService = new RateLimiterService();
    }

    @Test
    void tryConsume_WithinLimit() {
        // Act & Assert
        assertTrue(rateLimiterService.tryConsume("test-client"));
        assertTrue(rateLimiterService.tryConsume("test-client"));
        assertTrue(rateLimiterService.tryConsume("test-client"));
    }

    @Test
    void tryConsume_DifferentClients() {
        // Act & Assert
        assertTrue(rateLimiterService.tryConsume("client-1"));
        assertTrue(rateLimiterService.tryConsume("client-2"));
        assertTrue(rateLimiterService.tryConsume("client-3"));
    }
} 