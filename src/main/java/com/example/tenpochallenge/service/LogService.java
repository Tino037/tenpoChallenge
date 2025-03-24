package com.example.tenpochallenge.service;

import com.example.tenpochallenge.model.RequestLog;
import com.example.tenpochallenge.repository.RequestLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogService {

    @Autowired
    private RequestLogRepository requestLogRepository;

    public void saveLog(double number, double percentage, String result, String clientIp) {
        RequestLog log = new RequestLog();
        log.setNumber(number);
        log.setPercentage(percentage);
        log.setResult(result);
        log.setStatus("SUCCESS");
        log.setTimestamp(LocalDateTime.now());
        log.setClientIp(clientIp);
        requestLogRepository.save(log);
    }
} 