package com.example.mshotelrooms.dto;

import com.example.mshotelrooms.entity.Reservation;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record ReservationResponseDto(
        String id,
        String guestName,
        String guestEmail,
        String roomNumber,
        String roomType,
        LocalDate checkIn,
        LocalDate checkOut,
        Integer guests,
        BigDecimal totalAmount,
        String status,
        String notes,
        Instant requestedAt,
        Instant updatedAt
) {
    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getGuestName(),
                reservation.getGuestEmail(),
                reservation.getRoomNumber(),
                reservation.getRoomType(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getGuests(),
                reservation.getTotalAmount(),
                reservation.getStatus(),
                reservation.getNotes(),
                reservation.getRequestedAt(),
                reservation.getUpdatedAt());
    }
}
