package com.example.tenpochallenge.exception;

public class ApiException extends RuntimeException {
    
    private final ErrorType errorType;
    private final String customMessage;
    
    public ApiException(ErrorType errorType) {
        super(errorType.getDefaultMessage());
        this.errorType = errorType;
        this.customMessage = errorType.getDefaultMessage();
    }
    
    public ApiException(ErrorType errorType, String customMessage) {
        super(customMessage);
        this.errorType = errorType;
        this.customMessage = customMessage;
    }
    
    public ApiException(ErrorType errorType, String customMessage, Throwable cause) {
        super(customMessage, cause);
        this.errorType = errorType;
        this.customMessage = customMessage;
    }
    
    public ErrorType getErrorType() {
        return errorType;
    }
    
    public String getCustomMessage() {
        return customMessage;
    }
} 