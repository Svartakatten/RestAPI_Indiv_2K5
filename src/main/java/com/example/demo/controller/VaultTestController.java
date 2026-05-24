package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/vault-test")
public class VaultTestController {

    private final String testPassword;

    public VaultTestController(@Value("${test.password}") String testPassword) {
        this.testPassword = testPassword;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> verifyVaultConnection() {
        Map<String, String> response = Map.of(
                "status", "Vault Integration Successful",
                "retrieved_secret", this.testPassword
        );
        
        return ResponseEntity.ok(response);
    }
}