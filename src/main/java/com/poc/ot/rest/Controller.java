package com.poc.ot.rest;

import com.poc.ot.entity.CustomerCommand;
import com.poc.ot.handler.CustomerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<?> saveCustomer() throws Throwable {
        try {
            logger.info("Incoming request at {} for request /save", applicationName);
            return ResponseEntity.ok("Customer saved");
        } catch (Throwable throwable) {
            logger.error("Error in /save: {}", throwable.getMessage());
            throw throwable; // Delegate to GlobalExceptionHandler
        }
    }

    @PostMapping("/find")
    public ResponseEntity<?> findCustomerById(@RequestBody CustomerCommand request) {
        try {
            logger.info("Incoming request to find customer with ID: {}", request.getId());

            if (request.getId() == 1) {
                return ResponseEntity.ok("Customer found");
            } else {
                throw new CustomerNotFoundException("Customer not found");
            }
        } catch (CustomerNotFoundException ex) {
            logger.warn("Error finding customer: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Throwable throwable) {
            logger.error("Unexpected error: {}", throwable.getMessage(), throwable);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + throwable.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCustomer() throws Throwable {
        try {
            logger.info("Incoming request at {} for request /delete", applicationName);
            return ResponseEntity.ok("Customer deleted");
        } catch (Throwable throwable) {
            logger.error("Error in /delete: {}", throwable.getMessage());
            throw throwable; // Delegate to GlobalExceptionHandler
        }
    }

    @GetMapping("/interaction1")
    public ResponseEntity<?> path1() throws Throwable {
        try {
            logger.info("Incoming request at {} for request /interaction1", applicationName);
            String response = restTemplate.getForObject("http://localhost:8090/service/interaction2", String.class);
            return ResponseEntity.ok("response from /interaction1 + " + response);
        } catch (Throwable throwable) {
            logger.error("Error in /interaction1: {}", throwable.getMessage());
            throw throwable; // Delegate to GlobalExceptionHandler
        }
    }

    @GetMapping("/interaction2")
    public ResponseEntity<?> path2() throws Throwable {
        try {
            logger.info("Incoming request at {} at /interaction2", applicationName);
            return ResponseEntity.ok("response from /interaction2 ");
        } catch (Throwable throwable) {
            logger.error("Error in /interaction2: {}", throwable.getMessage());
            throw throwable; // Delegate to GlobalExceptionHandler
        }
    }
}
