package com.example.tenpochallenge.service;

import com.example.tenpochallenge.model.PercentageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CalculationServiceTest {

    @Mock
    private ExternalPercentageService externalPercentageService;

    @Mock
    private LoggingService loggingService;

    private CalculationService calculationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        calculationService = new CalculationService(externalPercentageService, loggingService);
        when(loggingService.logRequest(any(), any(), any())).thenReturn(Mono.empty());
    }

    @Test
    void calculateFinalValue_Success() {
        // Arrange
        double value1 = 100.0;
        double value2 = 200.0;
        double percentage = 10.0;
        when(externalPercentageService.getExternalPercentage())
            .thenReturn(Mono.just(new PercentageResponse(percentage, false)));

        // Act & Assert
        StepVerifier.create(calculationService.calculateFinalValue(value1, value2))
            .expectNext("Resultado: 330.00 (usando porcentaje: 10.00% desde servicio externo)")
            .verifyComplete();
    }

    @Test
    void calculateFinalValue_WithCache() {
        // Arrange
        double value1 = 100.0;
        double value2 = 200.0;
        double percentage = 10.0;
        when(externalPercentageService.getExternalPercentage())
            .thenReturn(Mono.just(new PercentageResponse(percentage, true)));

        // Act & Assert
        StepVerifier.create(calculationService.calculateFinalValue(value1, value2))
            .expectNext("Resultado: 330.00 (usando porcentaje: 10.00% desde caché)")
            .verifyComplete();
    }

    @Test
    void calculateFinalValue_ExternalServiceError() {
        // Arrange
        double value1 = 100.0;
        double value2 = 200.0;
        when(externalPercentageService.getExternalPercentage())
            .thenReturn(Mono.error(new RuntimeException("Error de servicio externo")));

        // Act & Assert
        StepVerifier.create(calculationService.calculateFinalValue(value1, value2))
            .expectNext("Error: Error de servicio externo")
            .verifyComplete();
    }

    @Test
    void calculateFinalValue_ZeroValues() {
        // Arrange
        double value1 = 0.0;
        double value2 = 0.0;
        double percentage = 10.0;
        when(externalPercentageService.getExternalPercentage())
            .thenReturn(Mono.just(new PercentageResponse(percentage, false)));

        // Act & Assert
        StepVerifier.create(calculationService.calculateFinalValue(value1, value2))
            .expectNext("Resultado: 0.00 (usando porcentaje: 10.00% desde servicio externo)")
            .verifyComplete();
    }
} 