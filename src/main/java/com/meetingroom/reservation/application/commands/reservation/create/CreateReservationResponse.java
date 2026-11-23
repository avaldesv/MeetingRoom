package com.meetingroom.reservation.application.commands.reservation.create;

import com.meetingroom.reservation.domain.entities.ReservationEntity;
import com.meetingroom.reservation.domain.entities.ReservationStatus;

import java.time.Instant;
import java.util.UUID;

public record CreateReservationResponse(
        UUID id,
        UUID roomId,
        String responsible,
        Instant startAt,
        Instant endAt,
        ReservationStatus status,
        Instant createdAt,
        Instant cancelledAt
) {
    public static CreateReservationResponse from(ReservationEntity entity) {
        return new CreateReservationResponse(
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
