package com.example.tenpochallenge.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CacheResponse {
    private Double percentage;
    private String message;
    private LocalDateTime timestamp;
} 