package com.meetingroom.reservation.application.commands.reservation.create;

import com.meetingroom.common.application.Command;
import com.meetingroom.common.exception.BadRequestException;
import com.meetingroom.common.exception.ConflictException;
import com.meetingroom.common.exception.NotFoundException;
import com.meetingroom.reservation.domain.entities.ReservationEntity;
import com.meetingroom.reservation.domain.entities.ReservationStatus;
import com.meetingroom.reservation.domain.interfaces.ReservationRepository;
import com.meetingroom.room.domain.entities.RoomEntity;
import com.meetingroom.room.domain.interfaces.RoomRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class CreateReservationCommandHandler
        implements Command<CreateReservationRequest, CreateReservationResponse> {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public CreateReservationCommandHandler(
            ReservationRepository reservationRepository,
            RoomRepository roomRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public CreateReservationResponse execute(CreateReservationRequest request) {
        if (!request.endAt().isAfter(request.startAt())) {
            throw new BadRequestException("endAt debe ser posterior a startAt");
        }

        RoomEntity room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new NotFoundException("Sala no encontrada: " + request.roomId()));
        if (!room.isActive()) {
            throw new BadRequestException("La sala no está activa: " + request.roomId());
        }

        if (reservationRepository.existsOverlapping(request.roomId(), request.startAt(), request.endAt())) {
            throw new ConflictException("La sala ya está ocupada en ese horario");
        }

        ReservationEntity entity = new ReservationEntity();
        entity.setId(UUID.randomUUID());
        entity.setRoomId(request.roomId());
        entity.setResponsible(request.responsible().trim());
        entity.setStartAt(request.startAt());
        entity.setEndAt(request.endAt());
        entity.setStatus(ReservationStatus.ACTIVE);
        entity.setCreatedAt(Instant.now());

        return CreateReservationResponse.from(reservationRepository.save(entity));
    }
}
