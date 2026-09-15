package com.example.mshotelrooms.health;


import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mshotelrooms.health.dto.HealthResponseDto;

@RestController
@RequestMapping("/api")
public class HealthCheckController {

    private final DataSource dataSource;

    public HealthCheckController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponseDto> getHealth() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
            return ResponseEntity.ok(new HealthResponseDto("UP"));
            }
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new HealthResponseDto("DOWN", "Database connection is invalid"));
        } catch (Exception e){
            HealthResponseDto errorResponse = new HealthResponseDto("DOWN", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }
    }
}
