package com.example.backend;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class FetchController {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String WALMART_URL = "https://locationplatform.search.services.prod.walmart.com/location/searchThirdPartyBu?q=number%3A{storeId}&q=fetchFromDB%3Atrue";

    @GetMapping("/store/validate")
    public ResponseEntity<StoreValidationResponse> validateStore(@RequestParam String storeId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("WM_CONSUMER.ID", "6a21c103-3bd2-4994-aa11-2606005738bb");
            headers.set("WM_SVC.ENV", "prod");
            headers.set("WM_SVC.NAME", "LOCATION-PLATFORM-SEARCH-MDM");
            headers.set("subscription-id", "e0effb9f-fc20-480e-b1e1-cc59d2e30233");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            String url = WALMART_URL.replace("{storeId}", storeId);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
            );

            StoreValidationResponse validationResponse = new StoreValidationResponse();
            validationResponse.setRaw(response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();
                List<Map<String, Object>> stores = (List<Map<String, Object>>) body.get("locations");
                
                if (stores != null && !stores.isEmpty()) {
                    validationResponse.setStatus("GREEN");
                } else {
                    validationResponse.setStatus("RED");
                    validationResponse.setIssues(Arrays.asList("Store not found"));
                }
            } else {
                validationResponse.setStatus("RED");
                validationResponse.setIssues(Arrays.asList("Error fetching store data"));
            }

            return ResponseEntity.ok(validationResponse);
        } catch (Exception e) {
            StoreValidationResponse errorResponse = new StoreValidationResponse();
            errorResponse.setStatus("RED");
            errorResponse.setIssues(Arrays.asList("Error: " + e.getMessage()));
            return ResponseEntity.ok(errorResponse);
        }
    }
}

class StoreValidationResponse {
    private String status;
    private List<String> issues;
    private Map<String, Object> raw;

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getIssues() { return issues; }
    public void setIssues(List<String> issues) { this.issues = issues; }
    public Map<String, Object> getRaw() { return raw; }
    public void setRaw(Map<String, Object> raw) { this.raw = raw; }
}
