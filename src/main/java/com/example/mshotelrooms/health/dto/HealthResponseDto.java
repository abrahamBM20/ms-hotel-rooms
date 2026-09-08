package com.example.mshotelrooms.health.dto;

public record HealthResponseDto (String status, String error){
    public HealthResponseDto(String status){
        this(status, null);
    }
}
