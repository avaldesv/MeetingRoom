package com.meetingroom.reservation.infrastructure.persistence.repositories;

import com.meetingroom.reservation.domain.entities.ReservationStatus;
import com.meetingroom.reservation.infrastructure.persistence.models.ReservationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

public interface ReservationJpaRepository extends JpaRepository<ReservationModel, UUID> {

    @Query("""
            SELECT COUNT(r) > 0 FROM ReservationModel r
            WHERE r.roomId = :roomId
              AND r.status = com.meetingroom.reservation.domain.entities.ReservationStatus.ACTIVE
              AND r.startAt < :endAt
              AND r.endAt > :startAt
            """)
    boolean existsOverlapping(
            @Param("roomId") UUID roomId,
            @Param("startAt") Instant startAt,
            @Param("endAt") Instant endAt
    );

    @Query("""
            SELECT r FROM ReservationModel r
            WHERE (:roomId IS NULL OR r.roomId = :roomId)
              AND (:dateFrom IS NULL OR r.startAt >= :dateFrom)
              AND (:dateTo IS NULL OR r.startAt < :dateTo)
              AND (:status IS NULL OR r.status = :status)
            """)
    Page<ReservationModel> search(
            @Param("roomId") UUID roomId,
            @Param("dateFrom") Instant dateFrom,
            @Param("dateTo") Instant dateTo,
            @Param("status") ReservationStatus status,
            Pageable pageable
    );
}
