package com.example.tenpochallenge.controller;

import com.example.tenpochallenge.model.RequestLog;
import com.example.tenpochallenge.repository.RequestLogRepository;
import com.example.tenpochallenge.service.CacheService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class HistoryController {

    private static final Logger logger = LoggerFactory.getLogger(HistoryController.class);
    private final RequestLogRepository requestLogRepository;
    private final CacheService cacheService;

    public HistoryController(RequestLogRepository requestLogRepository, CacheService cacheService) {
        this.requestLogRepository = requestLogRepository;
        this.cacheService = cacheService;
    }

    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getRequestHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.debug("Obteniendo historial de requests - página: {}, tamaño: {}", page, size);
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<RequestLog> pageResult = requestLogRepository.findAll(pageRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("content", pageResult.getContent());
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cache")
    public Mono<ResponseEntity<Map<String, Object>>> getCacheHistory() {
        logger.debug("Consultando valor en caché");
        return cacheService.getPercentage()
                .map(percentage -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("percentage", percentage);
                    response.put("timestamp", LocalDateTime.now());
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.ok(createEmptyCacheResponse()));
    }

    private Map<String, Object> createEmptyCacheResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "No hay valor en caché");
        response.put("timestamp", LocalDateTime.now());
        return response;
    }
}
