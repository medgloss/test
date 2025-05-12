package com.example.backend;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@RestController
@RequestMapping("/api")
public class FetchController {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String WALMART_URL = "https://locationplatform.search.services.prod.walmart.com/location/searchThirdPartyBu?q=number%3A1524353&q=fetchFromDB%3Atrue";

    @GetMapping("/fetch-data")
    public ResponseEntity<String> fetchData() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("WM_CONSUMER.ID", "6a21c103-3bd2-4994-aa11-2606005738bb");
            headers.set("WM_SVC.ENV", "prod");
            headers.set("WM_SVC.NAME", "LOCATION-PLATFORM-SEARCH-MDM");
            headers.set("subscription-id", "e0effb9f-fc20-480e-b1e1-cc59d2e30233");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                WALMART_URL,
                HttpMethod.GET,
                entity,
                String.class
            );

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching data: " + e.getMessage());
        }
    }
}
