package com.poc.ot.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/customer")
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private final RestTemplate restTemplate;

    @Value("${spring.application.name}")
    private String applicationName;

    public Controller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCustomer() {
        try {
            logger.info("Incoming request at {} for request /save", applicationName);
            return ResponseEntity.ok("Customer saved");
        } catch (Exception e) {
            logger.error("Error occurred while saving customer: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save customer");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCustomer() {
        try {
            logger.info("Incoming request at {} for request /delete", applicationName);
            return ResponseEntity.ok("Customer deleted");
        } catch (Exception e) {
            logger.error("Error occurred while deleting customer: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete customer");
        }
    }

    @GetMapping("/interaction1")
    public ResponseEntity<?> path1() {
        try {
            logger.info("Incoming request at {} for request /interaction1", applicationName);
            String response = restTemplate.getForObject("http://localhost:8090/service/interaction2", String.class);
            return ResponseEntity.ok("response from /interaction1 + " + response);
        } catch (HttpClientErrorException e) {
            logger.error("Client error occurred while calling interaction2: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body("Failed to call interaction2: " + e.getMessage());
        } catch (ResourceAccessException ex) {
            logger.error("Resource access error while calling interaction2: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Interaction2 service is unavailable");
        } catch (Exception ex) {
            logger.error("Unexpected error occurred while calling interaction2: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/interaction2")
    public ResponseEntity<?> path2() {
        try {
            logger.info("Incoming request at {} at /interaction2", applicationName);
            return ResponseEntity.ok("response from /interaction2 ");
        } catch (Exception ex) {
            logger.error("Error occurred while handling interaction2: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to handle interaction2");
        }
    }
}
