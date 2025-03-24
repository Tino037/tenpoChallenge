package com.example.tenpochallenge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Respuesta de error estándar")
public class ErrorResponse {
    @Schema(description = "Fecha y hora del error")
    private LocalDateTime timestamp;
    
    @Schema(description = "Código de estado HTTP")
    private int status;
    
    @Schema(description = "Tipo de error")
    private String error;
    
    @Schema(description = "Mensaje descriptivo del error")
    private String message;
} 