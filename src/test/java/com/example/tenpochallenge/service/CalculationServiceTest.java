package com.example.tenpochallenge.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

    @Mock
    private ExternalPercentageService externalPercentageService;

    @Mock
    private LoggingService loggingService;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private CalculationService calculationService;

    @BeforeEach
    void setUp() {
        when(loggingService.logRequest(anyString(), anyString(), anyString()))
            .thenReturn(Mono.empty());
    }

    @Test
    void calculateFinalValue_Success() {
        // Arrange
        when(externalPercentageService.getExternalPercentage()).thenReturn(Mono.just(10.0));

        // Act & Assert
        StepVerifier.create(calculationService.calculateFinalValue(100.0, 50.0))
            .assertNext(result -> {
                assert result.contains("Resultado: 165,00");
                assert result.contains("usando porcentaje: 10,00%");
            })
            .verifyComplete();
    }

    @Test
    void calculateFinalValue_ExternalServiceFailure() {
        // Arrange
        when(externalPercentageService.getExternalPercentage())
            .thenReturn(Mono.error(new RuntimeException("No hay porcentaje disponible")));

        // Act & Assert
        StepVerifier.create(calculationService.calculateFinalValue(100.0, 50.0))
            .assertNext(result -> {
                assert result.contains("Error: No hay porcentaje disponible");
            })
            .verifyComplete();
    }

    @Test
    void calculateFinalValue_LoggingFailure() {
        // Arrange
        when(externalPercentageService.getExternalPercentage()).thenReturn(Mono.just(10.0));
        when(loggingService.logRequest(anyString(), anyString(), anyString()))
            .thenReturn(Mono.error(new RuntimeException("Error al guardar log")));

        // Act & Assert
        StepVerifier.create(calculationService.calculateFinalValue(100.0, 50.0))
            .assertNext(result -> {
                assert result.contains("Resultado: 165,00");
                assert result.contains("usando porcentaje: 10,00%");
            })
            .verifyComplete();
    }

    @Test
    void calculateFinalValue_ZeroValues() {
        // Arrange
        when(externalPercentageService.getExternalPercentage()).thenReturn(Mono.just(10.0));

        // Act & Assert
        StepVerifier.create(calculationService.calculateFinalValue(0.0, 0.0))
            .assertNext(result -> {
                assert result.contains("Resultado: 0,00");
                assert result.contains("usando porcentaje: 10,00%");
            })
            .verifyComplete();
    }

    @Test
    void calculateFinalValue_NegativeValues() {
        // Arrange
        when(externalPercentageService.getExternalPercentage()).thenReturn(Mono.just(10.0));

        // Act & Assert
        StepVerifier.create(calculationService.calculateFinalValue(-100.0, -50.0))
            .assertNext(result -> {
                assert result.contains("Resultado: -165,00");
                assert result.contains("usando porcentaje: 10,00%");
            })
            .verifyComplete();
    }
} 