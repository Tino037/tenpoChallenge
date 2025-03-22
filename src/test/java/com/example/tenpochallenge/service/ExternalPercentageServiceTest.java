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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalPercentageServiceTest {

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private ExternalPercentageService externalPercentageService;

    @Test
    void getExternalPercentage_Success() {
        // Arrange
        when(cacheService.setPercentage(any())).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(externalPercentageService.getExternalPercentage())
                .expectNext(10.0)
                .verifyComplete();
    }

    @Test
    void getExternalPercentage_CacheSetFailure() {
        // Arrange
        when(cacheService.setPercentage(any())).thenReturn(Mono.error(new RuntimeException("Error al guardar en caché")));
        when(cacheService.getPercentage()).thenReturn(Mono.just(10.0));

        // Act & Assert
        StepVerifier.create(externalPercentageService.getExternalPercentage())
                .expectNext(10.0)
                .verifyComplete();
    }

    @Test
    void getExternalPercentage_CacheGetFailure() {
        // Arrange
        when(cacheService.setPercentage(any())).thenReturn(Mono.error(new RuntimeException("Error al guardar en caché")));
        when(cacheService.getPercentage()).thenReturn(Mono.error(new RuntimeException("Error al obtener de caché")));

        // Act & Assert
        StepVerifier.create(externalPercentageService.getExternalPercentage())
                .expectErrorMatches(error -> 
                    error instanceof RuntimeException && 
                    error.getMessage().equals("No hay porcentaje disponible"))
                .verify();
    }

    @Test
    void getExternalPercentage_CacheEmpty() {
        // Arrange
        when(cacheService.setPercentage(any())).thenReturn(Mono.error(new RuntimeException("Error al guardar en caché")));
        when(cacheService.getPercentage()).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(externalPercentageService.getExternalPercentage())
                .expectErrorMatches(error -> 
                    error instanceof RuntimeException && 
                    error.getMessage().equals("No hay porcentaje disponible"))
                .verify();
    }
} 