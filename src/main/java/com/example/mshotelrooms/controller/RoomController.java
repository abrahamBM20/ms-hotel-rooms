package com.example.mshotelrooms.controller;

import com.example.mshotelrooms.dto.RoomRequestDto;
import com.example.mshotelrooms.entity.Room;
import com.example.mshotelrooms.repository.RoomRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // El servicio es privado: la autenticación y autorización las realiza el BFF.

    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable UUID id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habitación no encontrada"));
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@Valid @RequestBody RoomRequestDto dto) {
        if (roomRepository.existsByRoomNumber(dto.roomNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de habitación ya existe");
        }

        Room room = new Room();
        room.setRoomNumber(dto.roomNumber());
        room.setRoomType(dto.roomType());
        room.setPricePerNight(dto.pricePerNight());
        room.setIsAvailable(dto.isAvailable() != null ? dto.isAvailable() : true);
        room.setDescription(dto.description());
        // Se guarda como texto plano (ej: https://ejemplo.com/imagen.jpg)
        room.setImageUrl(dto.imageUrl());

        return ResponseEntity.status(HttpStatus.CREATED).body(roomRepository.save(room));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable UUID id, @Valid @RequestBody RoomRequestDto dto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habitación no encontrada"));

        room.setRoomNumber(dto.roomNumber());
        room.setRoomType(dto.roomType());
        room.setPricePerNight(dto.pricePerNight());
        if (dto.isAvailable() != null) {
            room.setIsAvailable(dto.isAvailable());
        }
        room.setDescription(dto.description());
        if (dto.imageUrl() != null) {
            room.setImageUrl(dto.imageUrl());
        }

        return ResponseEntity.ok(roomRepository.save(room));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable UUID id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habitación no encontrada"));
        
        // Gracias a @SQLDelete en Room.java, esto hace un soft-delete automático
        roomRepository.delete(room);
        
        return ResponseEntity.noContent().build();
    }
}