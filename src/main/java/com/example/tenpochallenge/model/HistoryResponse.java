package com.example.tenpochallenge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Respuesta del historial de operaciones")
public class HistoryResponse {
    @Schema(description = "Lista de operaciones")
    private List<RequestLog> content;
    
    @Schema(description = "Número de página actual")
    private int currentPage;
    
    @Schema(description = "Total de elementos")
    private long totalItems;
    
    @Schema(description = "Total de páginas")
    private int totalPages;
} 