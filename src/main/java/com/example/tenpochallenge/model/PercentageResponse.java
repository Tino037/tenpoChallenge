package com.example.tenpochallenge.model;

public class PercentageResponse {
    private final double percentage;
    private final boolean fromCache;

    public PercentageResponse(double percentage, boolean fromCache) {
        this.percentage = percentage;
        this.fromCache = fromCache;
    }

    public double getPercentage() {
        return percentage;
    }

    public boolean isFromCache() {
        return fromCache;
    }
} 