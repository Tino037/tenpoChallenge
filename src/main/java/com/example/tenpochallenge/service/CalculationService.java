package com.example.tenpochallenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.text.DecimalFormat;

@Service
public class CalculationService {

	private static final Logger logger = LoggerFactory.getLogger(CalculationService.class);
	private final ExternalPercentageService externalPercentageService;
	private final LoggingService loggingService;

	public CalculationService(
			ExternalPercentageService externalPercentageService,
			LoggingService loggingService) {
		this.externalPercentageService = externalPercentageService;
		this.loggingService = loggingService;
	}

	public Mono<String> calculateFinalValue(double value1, double value2) {
		String requestParams = String.format("value1=%.2f, value2=%.2f", value1, value2);
		
		return externalPercentageService.getExternalPercentage()
				.map(percentageResponse -> {
					double result = calculateResult(value1, value2, percentageResponse.getPercentage());
					return formatResponse(result, percentageResponse.getPercentage(), percentageResponse.isFromCache());
				})
				.onErrorResume(error -> {
					logger.error("Error en el cálculo", error);
					return Mono.just("Error: " + error.getMessage());
				})
				.flatMap(response -> logAndReturnResponse(requestParams, response));
	}

	private double calculateResult(double value1, double value2, double percentage) {
		double sum = value1 + value2;
		return sum + (sum * percentage / 100);
	}

	private String formatResponse(double result, double percentage, boolean fromCache) {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		String source = fromCache ? "caché" : "servicio externo";
		return String.format("Resultado: %s (usando porcentaje: %.2f%% desde %s)", 
			df.format(result), percentage, source);
	}

	private Mono<String> logAndReturnResponse(String requestParams, String response) {
		return loggingService.logRequest("/tenpo/addPorcentage", requestParams, response)
				.thenReturn(response);
	}
}
