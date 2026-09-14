package com.example.mshotelrooms.dto;

import java.math.BigDecimal;

import com.example.mshotelrooms.entity.RoomType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RoomRequestDto(
        @NotBlank String roomNumber,
        @NotNull RoomType roomType,
        @NotNull @Positive BigDecimal pricePerNight,
        Boolean isAvailable,
        String description,
        String imageUrl
) {}