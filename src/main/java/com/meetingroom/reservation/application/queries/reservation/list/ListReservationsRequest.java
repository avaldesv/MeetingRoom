package com.meetingroom.reservation.application.queries.reservation.list;

import com.meetingroom.reservation.domain.entities.ReservationStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ListReservationsRequest(
        UUID roomId,
        LocalDate date,
        ReservationStatus status,
        List<String> filters,
        List<String> ordersBy
) {
}
