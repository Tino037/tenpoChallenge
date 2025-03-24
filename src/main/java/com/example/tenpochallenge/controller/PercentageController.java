package com.example.tenpochallenge.controller;

import com.example.tenpochallenge.exception.ApiException;
import com.example.tenpochallenge.exception.ErrorType;
import com.example.tenpochallenge.service.CalculationService;
import com.example.tenpochallenge.service.ExternalPercentageService;
import com.example.tenpochallenge.service.RateLimiterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tenpo")
public class PercentageController {

    private static final Logger logger = LoggerFactory.getLogger(PercentageController.class);
    private final CalculationService calculationService;
    private final ExternalPercentageService externalService;
    private final RateLimiterService rateLimiterService;

    public PercentageController(
            CalculationService calculationService,
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
        
        // Validar inputs
        if (value1 <= 0 || value2 <= 0) {
            throw new ApiException(
                ErrorType.INVALID_INPUT,
                "Los valores de entrada deben ser mayores que 0"
            );
        }

        String ip = clientIp != null ? clientIp : "unknown";
        
        // Verificar rate limit
        rateLimiterService.tryConsume(ip);

        return calculationService.calculateFinalValue(value1, value2)
            .map(result -> ResponseEntity.ok(result))
            .onErrorResume(e -> Mono.error(new ApiException(
                ErrorType.EXTERNAL_SERVICE_ERROR,
                "Error al obtener el porcentaje del servicio externo",
                e
            )));
    }
}

