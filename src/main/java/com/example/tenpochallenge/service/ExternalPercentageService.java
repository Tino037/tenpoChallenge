package com.example.tenpochallenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ExternalPercentageService {

	private static final Logger logger = LoggerFactory.getLogger(ExternalPercentageService.class);
	private static final double FIXED_PERCENTAGE = 10.0;
	
	private final CacheService cacheService;

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

	public static class PercentageResponse {
		private final double percentage;
		private final boolean fromCache;

		public PercentageResponse(double percentage, boolean fromCache) {
			this.percentage = percentage;
			this.fromCache = fromCache;
		}

		public double getPercentage() {
			return percentage;
		}

		public boolean isFromCache() {
			return fromCache;
		}
	}
}
