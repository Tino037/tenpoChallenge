package com.example.tenpochallenge.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;

@Data
@Schema(description = "Estructura de respuesta para errores de la API")
public class ErrorResponse {
    @Schema(description = "Momento en que ocurrió el error")
    private final LocalDateTime timestamp;
    
    @Schema(description = "Código de estado HTTP")
    private final int status;
    
    @Schema(description = "Tipo de error")
    private final String error;
    
    @Schema(description = "Mensaje descriptivo del error")
    private final String message;

    public ErrorResponse(ErrorType errorType, String customMessage) {
        this.timestamp = LocalDateTime.now();
        this.status = errorType.getHttpStatus().value();
        this.error = errorType.name();
        this.message = customMessage != null ? customMessage : errorType.getDefaultMessage();
    }

    public ErrorResponse(ErrorType errorType) {
        this(errorType, null);
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return String.format(
                "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"message\":\"%s\"}",
                timestamp, status, error, message
            );
        }
    }
} 