package com.example.tenpochallenge.service;

import com.example.tenpochallenge.model.PercentageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class ExternalPercentageService {

	private static final Logger logger = LoggerFactory.getLogger(ExternalPercentageService.class);
	private static final double FIXED_PERCENTAGE = 10.0;
	
	private final CacheService cacheService;
	private Double cachedPercentage;

	public ExternalPercentageService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	public Mono<PercentageResponse> getExternalPercentage() {
		return cacheService.getPercentage()
			.map(percentage -> new PercentageResponse(percentage, true))
			.switchIfEmpty(
				Mono.just(FIXED_PERCENTAGE)
					.flatMap(percentage -> 
						cacheService.setPercentage(percentage)
							.thenReturn(new PercentageResponse(percentage, false))
					)
					.onErrorResume(error -> {
						logger.error("Error al obtener porcentaje del servicio externo", error);
						return Mono.error(new RuntimeException("No hay porcentaje disponible"));
					})
			);
	}

	@Cacheable(value = "percentageCache")
	public Double getPercentage() {
		if (cachedPercentage != null) {
			return cachedPercentage;
		}

		try {
			// Simulación de llamada al servicio externo
			cachedPercentage = FIXED_PERCENTAGE;
			return cachedPercentage;
		} catch (Exception e) {
			throw new RuntimeException("Error al obtener el porcentaje del servicio externo", e);
		}
	}

	@Scheduled(fixedRate = 300000) // 5 minutos
	@CacheEvict(value = "percentageCache")
	public void clearCache() {
		cachedPercentage = null;
	}
}
