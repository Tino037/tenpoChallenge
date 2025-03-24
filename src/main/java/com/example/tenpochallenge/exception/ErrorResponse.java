package com.example.tenpochallenge.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;

@Data
@Schema(description = "Estructura de respuesta para errores de la API")
public class ErrorResponse {
      
    @Schema(description = "Mensaje descriptivo del error")
    private final String message;

    public ErrorResponse(ErrorType errorType, String customMessage) {
        this.message = customMessage != null ? customMessage : errorType.getDefaultMessage();
    }

    public ErrorResponse(ErrorType errorType) {
        this(errorType, null);
    }

    @Override
    public String toString() {
       return message;
    }
} 