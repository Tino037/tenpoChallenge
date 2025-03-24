package com.example.tenpochallenge.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    // Errores 4xx - Cliente
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "Los valores de entrada son inválidos"),
    RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "Has excedido el límite de peticiones"),
    INVALID_PARAMETERS(HttpStatus.BAD_REQUEST, "Los parámetros proporcionados son inválidos"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "El recurso solicitado no existe"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "No autorizado para realizar esta operación"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "No tienes permiso para acceder a este recurso"),
    
    // Errores 5xx - Servidor
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor"),
    EXTERNAL_SERVICE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "Error al comunicarse con el servicio externo"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Error al acceder a la base de datos"),
    CACHE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Error al acceder al caché");

    private final HttpStatus httpStatus;
    private final String defaultMessage;

    ErrorType(HttpStatus httpStatus, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
} 