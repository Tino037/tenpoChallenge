package com.example.tenpochallenge.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "request_log")
@AllArgsConstructor
@NoArgsConstructor
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String endpoint;

    @Column(nullable = false)
    private String parameters;

    @Column(nullable = false)
    private String response;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public RequestLog(String endpoint, String parameters, String response, LocalDateTime timestamp) {
        this.endpoint = endpoint;
        this.parameters = parameters;
        this.response = response;
        this.timestamp = timestamp;
    }

    // Getters y Setters
}
