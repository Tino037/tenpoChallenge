package com.example.tenpochallenge.controller;

import com.example.tenpochallenge.exception.ApiException;
import com.example.tenpochallenge.exception.ErrorType;
import com.example.tenpochallenge.model.RequestLog;
import com.example.tenpochallenge.service.CacheService;
import com.example.tenpochallenge.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/info")
public class HistoryController {

    private static final Logger logger = LoggerFactory.getLogger(HistoryController.class);
    private final HistoryService historyService;
    private final CacheService cacheService;

    public HistoryController(HistoryService historyService, CacheService cacheService) {
        this.historyService = historyService;
        this.cacheService = cacheService;
    }

    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getRequestHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Obteniendo historial de operaciones - Página: {}, Tamaño: {}", page, size);
            return ResponseEntity.ok(historyService.getHistory(page, size));
        } catch (Exception e) {
            logger.error("Error al obtener historial de operaciones", e);
            throw new ApiException(
                ErrorType.INTERNAL_SERVER_ERROR,
                "Error al obtener el historial de operaciones",
                e
            );
        }
    }

    @GetMapping("/cache")
    public Mono<ResponseEntity<Map<String, Object>>> getCacheHistory() {
        logger.info("Consultando valor en caché");
        return cacheService.getPercentage()
                .map(percentage -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("percentage", percentage);
                    response.put("timestamp", LocalDateTime.now());
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.ok(createEmptyCacheResponse()))
                .onErrorResume(e -> Mono.error(new ApiException(
                    ErrorType.INTERNAL_SERVER_ERROR,
                    "Error al obtener el valor del caché",
                    e
                )));
    }

    private Map<String, Object> createEmptyCacheResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "No hay valor en caché");
        response.put("timestamp", LocalDateTime.now());
        return response;
    }
}
