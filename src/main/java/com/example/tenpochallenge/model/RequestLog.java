package com.example.tenpochallenge.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "request_logs")
@Data
public class RequestLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private double number;
    
    @Column(nullable = false)
    private double percentage;
    
    @Column(nullable = false)
    private String result;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "client_ip")
    private String clientIp;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
