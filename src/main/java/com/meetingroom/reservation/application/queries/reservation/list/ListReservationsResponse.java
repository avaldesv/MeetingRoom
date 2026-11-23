package com.meetingroom.reservation.application.queries.reservation.list;

import com.meetingroom.reservation.domain.entities.ReservationEntity;
import com.meetingroom.reservation.domain.entities.ReservationStatus;

import java.time.Instant;
import java.util.UUID;

public record ListReservationsResponse(
        UUID id,
        UUID roomId,
        String responsible,
        Instant startAt,
        Instant endAt,
        ReservationStatus status,
        Instant createdAt,
        Instant cancelledAt
) {
    public static ListReservationsResponse from(ReservationEntity entity) {
        return new ListReservationsResponse(
                entity.getId(),
                entity.getRoomId(),
                entity.getResponsible(),
                entity.getStartAt(),
                entity.getEndAt(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getCancelledAt()
        );
    }
}
