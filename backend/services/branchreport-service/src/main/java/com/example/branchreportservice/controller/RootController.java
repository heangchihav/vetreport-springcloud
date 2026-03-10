package com.example.branchreportservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/branchreport")
public class RootController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "branchreport-service");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "1.0.0");
        return response;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "branchreport-service");
        response.put("description", "Branch Report Service for Product and Product Type Management");
        response.put("version", "1.0.0");
        response.put("endpoints", new String[] {
                "/api/products",
                "/api/product-types",
                "/api/health",
                "/api/info"
        });
        return response;
    }
}
