package com.example.tenpochallenge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Respuesta del estado del caché")
public class CacheResponse {
    @Schema(description = "Porcentaje almacenado en caché")
    private Double percentage;
    
    @Schema(description = "Mensaje cuando no hay valor en caché")
    private String message;
    
    @Schema(description = "Fecha y hora de la consulta")
    private LocalDateTime timestamp;
} 