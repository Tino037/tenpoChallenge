package com.example.tenpochallenge.exception;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ErrorResponse {
      
    private String message;
    private LocalDateTime timestamp;
    private int status;
    private String error;

    public ErrorResponse(ErrorType errorType, String customMessage) {
        this.message = customMessage != null ? customMessage : errorType.getDefaultMessage();
        this.timestamp = LocalDateTime.now();
        this.status = errorType.getHttpStatus().value();
        this.error = errorType.getHttpStatus().getReasonPhrase();
    }

    public ErrorResponse(ErrorType errorType) {
        this(errorType, null);
    }

    @Override
    public String toString() {
       return message;
    }
} 