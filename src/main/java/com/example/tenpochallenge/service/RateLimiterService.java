package com.example.tenpochallenge.service;

import com.example.tenpochallenge.exception.ApiException;
import com.example.tenpochallenge.exception.ErrorType;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {
    
    private static final int MAX_REQUESTS_PER_MINUTE = 3;
    private final Map<String, RequestCount> requestCounts = new ConcurrentHashMap<>();
    
    public void tryConsume(String clientIp) {
        RequestCount count = requestCounts.computeIfAbsent(clientIp, k -> new RequestCount());
        
        if (count.isExpired()) {
            count.reset();
        }
        
        if (count.getCount() >= MAX_REQUESTS_PER_MINUTE) {
            throw new ApiException(
                ErrorType.RATE_LIMIT_EXCEEDED,
                "Se ha excedido el límite de " + MAX_REQUESTS_PER_MINUTE + " peticiones por minuto"
            );
        }
        
        count.increment();
    }
    
    private static class RequestCount {
        private int count;
        private LocalDateTime lastReset;
        
        public RequestCount() {
            this.count = 0;
            this.lastReset = LocalDateTime.now();
        }
        
        public boolean isExpired() {
            return LocalDateTime.now().minusMinutes(1).isAfter(lastReset);
        }
        
        public void reset() {
            this.count = 0;
            this.lastReset = LocalDateTime.now();
        }
        
        public void increment() {
            this.count++;
        }
        
        public int getCount() {
            return count;
        }
    }
} 