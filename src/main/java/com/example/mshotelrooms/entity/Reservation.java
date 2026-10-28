package com.example.mshotelrooms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    private String id;

    @Column(name = "guest_name", nullable = false)
    private String guestName;

    @Column(name = "guest_email")
    private String guestEmail;

    @Column(name = "guest_id")
    private String guestId;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "check_in")
    private LocalDate checkIn;

    @Column(name = "check_out")
    private LocalDate checkOut;

    private Integer guests;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    private String status;
    private String notes;

    @Column(name = "requested_at")
    private Instant requestedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public String getId() { return id; }
    public String getGuestName() { return guestName; }
    public String getGuestEmail() { return guestEmail; }
    public String getGuestId() { return guestId; }
    public String getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public Integer getGuests() { return guests; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    public Instant getRequestedAt() { return requestedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
