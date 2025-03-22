package com.example.tenpochallenge.service;

import com.example.tenpochallenge.model.RequestLog;
import com.example.tenpochallenge.repository.RequestLogRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoggingService {

    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);
    private final RequestLogRepository requestLogRepository;

    public LoggingService(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    public Mono<Void> logRequest(String endpoint, String parameters, String response) {
        return Mono.fromCallable(() -> {
            logger.debug("Intentando guardar log en PostgreSQL - Endpoint: {}, Params: {}", endpoint, parameters);
            RequestLog log = new RequestLog(endpoint, parameters, response, LocalDateTime.now());
            RequestLog savedLog = requestLogRepository.save(log);
            logger.debug("Log guardado exitosamente en PostgreSQL con ID: {}", savedLog.getId());
            return savedLog;
        })
        .subscribeOn(Schedulers.boundedElastic())
        .doOnError(error -> {
            logger.error("Error al guardar el log en PostgreSQL: {}", error.getMessage());
            error.printStackTrace(); // Temporal para debug
        })
        .onErrorResume(error -> {
            logger.error("Error recuperable al guardar en PostgreSQL: {}", error.getMessage());
            return Mono.empty();
        })
        .then();
    }
}
