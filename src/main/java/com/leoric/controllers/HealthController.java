package com.leoric.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {
    private final DataSource dataSource;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("✅ API is up and running");
    }

    @GetMapping("/db")
    public ResponseEntity<String> healthDb() {
        try (Connection conn = dataSource.getConnection()) {
            return ResponseEntity.ok("✅ DB Connection OK: " + dbUrl);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("❌ DB Connection FAILED: " + e.getMessage());
        }
    }

    @GetMapping("/postman")
    public ResponseEntity<?> getPostmanCollection() {
        try {
            InputStream is = getClass().getResourceAsStream("/static/ProjectManagment.postman_collection.json");
            if (is == null) {
                return ResponseEntity.notFound().build();
            }
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return ResponseEntity.ok().body(json);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("❌ Failed to load Postman collection: " + e.getMessage());
        }
    }
}