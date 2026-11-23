package com.meetingroom.reservation.application.commands.reservation.cancel;

import com.meetingroom.reservation.domain.entities.ReservationEntity;
import com.meetingroom.reservation.domain.entities.ReservationStatus;

import java.time.Instant;
import java.util.UUID;

public record CancelReservationResponse(
        UUID id,
        UUID roomId,
        String responsible,
        Instant startAt,
        Instant endAt,
        ReservationStatus status,
        Instant createdAt,
        Instant cancelledAt
) {
    public static CancelReservationResponse from(ReservationEntity entity) {
        return new CancelReservationResponse(
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
