package com.example.branchreportservice.api.region;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/branchreport")
public class RegionController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String REGION_SERVICE_URL = "http://region-service:8086/api/region";

    @GetMapping("/simple-test")
    public ResponseEntity<?> simpleTest() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Simple test works - no RestTemplate");
        result.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Region controller is working");
        result.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/areas")
    public ResponseEntity<?> getAreas() {
        try {
            String url = REGION_SERVICE_URL + "/areas";
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", response.getBody());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> fallbackResponse = new HashMap<>();
            fallbackResponse.put("success", true);
            fallbackResponse.put("data", new Object[0]);

            Map<String, Object> errorInfo = new HashMap<>();
            errorInfo.put("warning", "Region service unavailable - returning empty areas");
            errorInfo.put("message", e.getMessage());
            fallbackResponse.put("meta", errorInfo);

            return ResponseEntity.ok(fallbackResponse);
        }
    }

    @GetMapping("/sub-areas")
    public ResponseEntity<?> getSubAreas() {
        try {
            String url = REGION_SERVICE_URL + "/sub-areas";
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", response.getBody());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> fallbackResponse = new HashMap<>();
            fallbackResponse.put("success", true);
            fallbackResponse.put("data", new Object[0]);

            Map<String, Object> errorInfo = new HashMap<>();
            errorInfo.put("warning", "Region service unavailable - returning empty sub-areas");
            errorInfo.put("message", e.getMessage());
            fallbackResponse.put("meta", errorInfo);

            return ResponseEntity.ok(fallbackResponse);
        }
    }

    @GetMapping("/branches")
    public ResponseEntity<?> getBranches() {
        try {
            String url = REGION_SERVICE_URL + "/branches";
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", response.getBody());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> fallbackResponse = new HashMap<>();
            fallbackResponse.put("success", true);
            fallbackResponse.put("data", new Object[0]);

            Map<String, Object> errorInfo = new HashMap<>();
            errorInfo.put("warning", "Region service unavailable - returning empty branches");
            errorInfo.put("message", e.getMessage());
            fallbackResponse.put("meta", errorInfo);

            return ResponseEntity.ok(fallbackResponse);
        }
    }
}
