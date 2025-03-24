package com.example.tenpochallenge.controller;

import com.example.tenpochallenge.exception.ApiException;
import com.example.tenpochallenge.exception.ErrorType;
import com.example.tenpochallenge.model.RequestLog;
import com.example.tenpochallenge.service.CacheService;
import com.example.tenpochallenge.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Historial y Caché", description = "API para consultar el historial de operaciones y estado del caché")
public class HistoryController {

    private static final Logger logger = LoggerFactory.getLogger(HistoryController.class);
    private final HistoryService historyService;
    private final CacheService cacheService;

    public HistoryController(HistoryService historyService, CacheService cacheService) {
        this.historyService = historyService;
        this.cacheService = cacheService;
    }

    @Operation(
        summary = "Obtener historial de operaciones",
        description = "Retorna un historial paginado de todas las operaciones realizadas"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Historial obtenido exitosamente",
            content = @Content(schema = @Schema(ref = "#/components/schemas/HistoryResponse"))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(ref = "#/components/schemas/ErrorResponse"))
        )
    })
    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getRequestHistory(
            @Parameter(description = "Número de página (0-based)", required = false, example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página", required = false, example = "10")
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

    @Operation(
        summary = "Obtener estado del caché",
        description = "Retorna el porcentaje actual almacenado en caché"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Estado del caché obtenido exitosamente",
            content = @Content(schema = @Schema(ref = "#/components/schemas/CacheResponse"))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(ref = "#/components/schemas/ErrorResponse"))
        )
    })
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
