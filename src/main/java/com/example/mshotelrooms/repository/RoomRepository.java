package com.example.mshotelrooms.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mshotelrooms.entity.Room;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    boolean existsByRoomNumber(String roomNumber);
}