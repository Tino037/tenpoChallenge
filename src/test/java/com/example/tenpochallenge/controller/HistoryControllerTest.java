package com.example.tenpochallenge.controller;

import com.example.tenpochallenge.model.RequestLog;
import com.example.tenpochallenge.service.CacheService;
import com.example.tenpochallenge.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HistoryControllerTest {

    @Mock
    private HistoryService historyService;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private HistoryController historyController;

    private RequestLog log1;
    private RequestLog log2;

    @BeforeEach
    void setUp() {
        log1 = new RequestLog();
        log1.setNumber(100.0);
        log1.setPercentage(50.0);
        log1.setResult("150.0");
        log1.setStatus("SUCCESS");
        log1.setClientIp("127.0.0.1");
        log1.setTimestamp(LocalDateTime.now());

        log2 = new RequestLog();
        log2.setNumber(200.0);
        log2.setPercentage(100.0);
        log2.setResult("300.0");
        log2.setStatus("SUCCESS");
        log2.setClientIp("127.0.0.1");
        log2.setTimestamp(LocalDateTime.now());
    }

    @Test
    void getRequestHistory_ShouldReturnHistory() {
        // Arrange
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("content", Arrays.asList(log1, log2));
        expectedResponse.put("currentPage", 0);
        expectedResponse.put("totalItems", 2L);
        expectedResponse.put("totalPages", 1);

        when(historyService.getHistory(anyInt(), anyInt())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = historyController.getRequestHistory(0, 10);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        List<RequestLog> content = (List<RequestLog>) body.get("content");
        assertEquals(2, content.size());
    }

    @Test
    void getCacheHistory_ShouldReturnCacheStatus() {
        // Arrange
        when(cacheService.getPercentage()).thenReturn(Mono.just(10.0));

        // Act & Assert
        StepVerifier.create(historyController.getCacheHistory())
                .expectNextMatches(response -> {
                    Map<String, Object> body = response.getBody();
                    assertNotNull(body);
                    assertEquals(10.0, body.get("percentage"));
                    assertNotNull(body.get("timestamp"));
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void getCacheHistory_WhenEmpty_ShouldReturnEmptyResponse() {
        // Arrange
        when(cacheService.getPercentage()).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(historyController.getCacheHistory())
                .expectNextMatches(response -> {
                    Map<String, Object> body = response.getBody();
                    assertNotNull(body);
                    assertEquals("No hay valor en caché", body.get("message"));
                    assertNotNull(body.get("timestamp"));
                    return true;
                })
                .verifyComplete();
    }
} 