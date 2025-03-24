package com.example.tenpochallenge.controller;

import com.example.tenpochallenge.exception.ApiException;
import com.example.tenpochallenge.exception.ErrorType;
import com.example.tenpochallenge.service.CalculationService;
import com.example.tenpochallenge.service.ExternalPercentageService;
import com.example.tenpochallenge.service.LogService;
import com.example.tenpochallenge.service.RateLimiterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tenpo")
@Tag(name = "Cálculo de Porcentajes", description = "API para realizar cálculos con porcentajes")
public class PercentageController {

    private final CalculationService calculationService;
    private final ExternalPercentageService externalService;
    private final LogService logService;
    private final RateLimiterService rateLimiterService;

    public PercentageController(
            CalculationService calculationService,
            ExternalPercentageService externalService,
            LogService logService,
            RateLimiterService rateLimiterService) {
        this.calculationService = calculationService;
        this.externalService = externalService;
        this.logService = logService;
        this.rateLimiterService = rateLimiterService;
    }

    @Operation(
        summary = "Calcular valor con porcentaje",
        description = "Calcula el resultado de sumar un porcentaje a un valor base. El porcentaje se obtiene de un servicio externo."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cálculo exitoso",
            content = @Content(schema = @Schema(type = "string"))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Los valores de entrada son inválidos (menores o iguales a 0)",
            content = @Content(schema = @Schema(ref = "#/components/schemas/ErrorResponse"))
        ),
        @ApiResponse(
            responseCode = "429",
            description = "Se ha excedido el límite de 3 peticiones por minuto",
            content = @Content(schema = @Schema(ref = "#/components/schemas/ErrorResponse"))
        ),
        @ApiResponse(
            responseCode = "503",
            description = "Error al comunicarse con el servicio externo de porcentajes",
            content = @Content(schema = @Schema(ref = "#/components/schemas/ErrorResponse"))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(ref = "#/components/schemas/ErrorResponse"))
        )
    })
    @GetMapping("/addPorcentage/{value1}/{value2}")
    public Mono<ResponseEntity<String>> addPercentage(
            @Parameter(description = "Valor base para el cálculo", required = true, example = "100.0")
            @PathVariable double value1,
            @Parameter(description = "Segundo valor para el cálculo", required = true, example = "50.0")
            @PathVariable double value2,
            @Parameter(description = "IP del cliente para rate limiting", required = false)
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
            .map(result -> {
                // Guardar el log usando el servicio
                logService.saveLog(value1, externalService.getPercentage(), result, ip);
                return ResponseEntity.ok(result);
            })
            .onErrorResume(e -> Mono.error(new ApiException(
                ErrorType.EXTERNAL_SERVICE_ERROR,
                "Error al obtener el porcentaje del servicio externo",
                e
            )));
    }
}

