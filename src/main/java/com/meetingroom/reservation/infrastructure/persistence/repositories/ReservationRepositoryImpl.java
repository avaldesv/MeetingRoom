package com.meetingroom.reservation.infrastructure.persistence.repositories;

import com.meetingroom.common.persistence.PageResult;
import com.meetingroom.reservation.domain.entities.ReservationEntity;
import com.meetingroom.reservation.domain.entities.ReservationStatus;
import com.meetingroom.reservation.domain.interfaces.ReservationRepository;
import com.meetingroom.reservation.infrastructure.persistence.mappers.ReservationMapper;
import com.meetingroom.reservation.infrastructure.persistence.models.ReservationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;
    private final ReservationMapper reservationMapper;

    public ReservationRepositoryImpl(
            ReservationJpaRepository reservationJpaRepository,
            ReservationMapper reservationMapper
    ) {
        this.reservationJpaRepository = reservationJpaRepository;
        this.reservationMapper = reservationMapper;
    }

    @Override
    public ReservationEntity save(ReservationEntity entity) {
        ReservationModel saved = reservationJpaRepository.save(reservationMapper.toModel(entity));
        return reservationMapper.toEntity(saved);
    }

    @Override
    public Optional<ReservationEntity> findById(UUID id) {
        return reservationJpaRepository.findById(id).map(reservationMapper::toEntity);
    }

    @Override
    public boolean existsOverlapping(UUID roomId, Instant startAt, Instant endAt) {
        return reservationJpaRepository.existsOverlapping(roomId, startAt, endAt);
    }

    @Override
    public PageResult<ReservationEntity> search(
            UUID roomId,
            Instant dateFrom,
            Instant dateTo,
            ReservationStatus status,
            int page,
            int size
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "startAt"));
        Page<ReservationModel> result = reservationJpaRepository.search(roomId, dateFrom, dateTo, status, pageable);
        return new PageResult<>(
                result.getContent().stream().map(reservationMapper::toEntity).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements()
        );
    }
}
