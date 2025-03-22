package com.example.tenpochallenge.controller;

import com.example.tenpochallenge.model.RequestLog;
import com.example.tenpochallenge.repository.RequestLogRepository;
import com.example.tenpochallenge.service.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HistoryControllerTest {

    @Mock
    private RequestLogRepository requestLogRepository;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private HistoryController historyController;

    private List<RequestLog> mockLogs;

    @BeforeEach
    void setUp() {
        mockLogs = List.of(
            new RequestLog("/test1", "param1", "response1", LocalDateTime.now()),
            new RequestLog("/test2", "param2", "response2", LocalDateTime.now().minusMinutes(1))
        );
    }

    @Test
    void getRequestHistory_ReturnsPagedResults() {
        // Arrange
        Page<RequestLog> pagedResponse = new PageImpl<>(mockLogs);
        when(requestLogRepository.findAll(any(PageRequest.class))).thenReturn(pagedResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = historyController.getRequestHistory(0, 10);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        
        @SuppressWarnings("unchecked")
        List<RequestLog> content = (List<RequestLog>) response.getBody().get("content");
        assertEquals(2, content.size());
        assertEquals(0, response.getBody().get("currentPage"));
        assertEquals(1, response.getBody().get("totalPages"));
        assertEquals(2L, response.getBody().get("totalElements"));
    }

    @Test
    void getRequestHistory_WithCustomPageSize() {
        // Arrange
        Page<RequestLog> pagedResponse = new PageImpl<>(mockLogs.subList(0, 1));
        when(requestLogRepository.findAll(any(PageRequest.class))).thenReturn(pagedResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = historyController.getRequestHistory(0, 1);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        
        @SuppressWarnings("unchecked")
        List<RequestLog> content = (List<RequestLog>) response.getBody().get("content");
        assertEquals(1, content.size());
    }

    @Test
    void getCacheHistory_WithCachedValue() {
        // Arrange
        double cachedValue = 10.5;
        when(cacheService.getPercentage()).thenReturn(Mono.just(cachedValue));

        // Act & Assert
        StepVerifier.create(historyController.getCacheHistory())
            .assertNext(response -> {
                assertTrue(response.getStatusCode().is2xxSuccessful());
                assertNotNull(response.getBody());
                assertEquals(cachedValue, response.getBody().get("percentage"));
                assertNotNull(response.getBody().get("timestamp"));
            })
            .verifyComplete();
    }

    @Test
    void getCacheHistory_WithoutCachedValue() {
        // Arrange
        when(cacheService.getPercentage()).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(historyController.getCacheHistory())
            .assertNext(response -> {
                assertTrue(response.getStatusCode().is2xxSuccessful());
                assertNotNull(response.getBody());
                assertEquals("No hay valor en caché", response.getBody().get("message"));
                assertNotNull(response.getBody().get("timestamp"));
            })
            .verifyComplete();
    }
} 