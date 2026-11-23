package com.meetingroom.reservation.domain.interfaces;

import com.meetingroom.common.persistence.PageResult;
import com.meetingroom.reservation.domain.entities.ReservationEntity;
import com.meetingroom.reservation.domain.entities.ReservationStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {

    ReservationEntity save(ReservationEntity entity);

    Optional<ReservationEntity> findById(UUID id);

    boolean existsOverlapping(UUID roomId, Instant startAt, Instant endAt);

    PageResult<ReservationEntity> search(
            UUID roomId,
            Instant dateFrom,
            Instant dateTo,
            ReservationStatus status,
            int page,
            int size
    );
}
