package com.example.tenpochallenge.model;

import lombok.Data;
import java.util.List;

@Data
public class HistoryResponse {
    private List<RequestLog> content;
    private int currentPage;
    private long totalItems;
    private int totalPages;
} 