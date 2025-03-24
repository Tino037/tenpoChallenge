package com.example.tenpochallenge.exception;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ErrorResponse response;
        
        if (ex instanceof ApiException) {
            ApiException apiException = (ApiException) ex;
            response = new ErrorResponse(apiException.getErrorType(), apiException.getMessage());
            exchange.getResponse().setStatusCode(apiException.getErrorType().getHttpStatus());
        } else {
            response = new ErrorResponse(ErrorType.INTERNAL_SERVER_ERROR);
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(response.toString().getBytes())));
    }
} 