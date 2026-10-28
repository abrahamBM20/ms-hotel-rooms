package com.example.mshotelrooms.repository;

import com.example.mshotelrooms.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    @Query("""
            SELECT r FROM Reservation r
            WHERE (:status IS NULL OR r.status = :status)
              AND (:roomNumber IS NULL OR r.roomNumber = :roomNumber)
              AND (:guestId IS NULL OR r.guestId = :guestId)
              AND (:id IS NULL OR r.id = :id)
            ORDER BY r.requestedAt DESC, r.createdAt DESC
            """)
    Page<Reservation> search(@Param("status") String status,
                             @Param("roomNumber") String roomNumber,
                             @Param("guestId") String guestId,
                             @Param("id") String id,
                             Pageable pageable);
}
