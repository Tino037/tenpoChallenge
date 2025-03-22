package com.example.tenpochallenge.controller;

import com.example.tenpochallenge.service.CalculationService;
import com.example.tenpochallenge.service.ExternalPercentageService;
import com.example.tenpochallenge.config.RateLimiterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tenpo")
public class PercentageController {

    private final CalculationService calculationService;
    private final ExternalPercentageService externalService;
    private final RateLimiterService rateLimiterService;

    public PercentageController(CalculationService calculationService, 
                              ExternalPercentageService externalService,
                              RateLimiterService rateLimiterService) {
        this.calculationService = calculationService;
        this.externalService = externalService;
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/addPorcentage/{value1}/{value2}")
    public Mono<ResponseEntity<String>> addPercentage(
            @PathVariable double value1,
            @PathVariable double value2,
            @RequestHeader(value = "X-Forwarded-For", required = false) String clientIp) {
        
        String ip = clientIp != null ? clientIp : "unknown";
        
        if (!rateLimiterService.tryConsume(ip)) {
            return Mono.just(ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Has excedido el límite de 3 peticiones por minuto"));
        }

        return calculationService.calculateFinalValue(value1, value2)
            .map(result -> ResponseEntity.ok(result));
    }
}

