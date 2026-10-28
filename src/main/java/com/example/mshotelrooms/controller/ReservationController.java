package com.example.mshotelrooms.controller;

import com.example.mshotelrooms.dto.ReservationResponseDto;
import com.example.mshotelrooms.repository.ReservationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private static final int DEFAULT_LIMIT = 100;
    private static final int MAX_LIMIT = 500;

    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Lectura síncrona para el BFF. Las escrituras siguen siendo responsabilidad
     * de SNS/SQS/Lambda; este servicio solo consulta la tabla compartida.
     */
    @GetMapping
    public List<ReservationResponseDto> list(@RequestParam(required = false) String status,
                                             @RequestParam(required = false) String roomNumber,
                                             @RequestParam(required = false) String guestId,
                                             @RequestParam(required = false) String id,
                                             @RequestParam(required = false) Integer limit) {
        int pageSize = limit == null || limit <= 0
                ? DEFAULT_LIMIT
                : Math.min(limit, MAX_LIMIT);

        return reservationRepository.search(
                        normalize(status),
                        normalize(roomNumber),
                        normalize(guestId),
                        normalize(id),
                        PageRequest.of(0, pageSize, Sort.unsorted()))
                .map(ReservationResponseDto::from)
                .getContent();
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
