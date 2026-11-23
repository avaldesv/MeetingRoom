package com.meetingroom.reservation;

import com.meetingroom.common.exception.BadRequestException;
import com.meetingroom.common.exception.ConflictException;
import com.meetingroom.common.exception.NotFoundException;
import com.meetingroom.reservation.application.commands.reservation.create.CreateReservationCommandHandler;
import com.meetingroom.reservation.application.commands.reservation.create.CreateReservationRequest;
import com.meetingroom.reservation.application.commands.reservation.create.CreateReservationResponse;
import com.meetingroom.reservation.domain.entities.ReservationEntity;
import com.meetingroom.reservation.domain.entities.ReservationStatus;
import com.meetingroom.reservation.domain.interfaces.ReservationRepository;
import com.meetingroom.room.domain.entities.RoomEntity;
import com.meetingroom.room.domain.interfaces.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateReservationCommandHandlerTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    private CreateReservationCommandHandler handler;

    private UUID roomId;
    private Instant startAt;
    private Instant endAt;

    @BeforeEach
    void setUp() {
        roomId = UUID.randomUUID();
        startAt = Instant.parse("2026-12-01T10:00:00Z");
        endAt = Instant.parse("2026-12-01T11:00:00Z");
        handler = new CreateReservationCommandHandler(reservationRepository, roomRepository);
    }

    @Test
    void execute_whenRoomOverlaps_throwsConflict() {
        CreateReservationRequest request = new CreateReservationRequest(
                roomId, "Ana Pérez", startAt, endAt
        );

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(activeRoom()));
        when(reservationRepository.existsOverlapping(roomId, startAt, endAt)).thenReturn(true);

        assertThatThrownBy(() -> handler.execute(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("La sala ya está ocupada en ese horario");
    }

    @Test
    void execute_whenEndAtNotAfterStartAt_throwsBadRequest() {
        CreateReservationRequest request = new CreateReservationRequest(
                roomId, "Ana Pérez", startAt, startAt
        );

        assertThatThrownBy(() -> handler.execute(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("endAt debe ser posterior a startAt");
    }

    @Test
    void execute_whenValid_persistsActiveReservation() {
        CreateReservationRequest request = new CreateReservationRequest(
                roomId, "Ana Pérez", startAt, endAt
        );

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(activeRoom()));
        when(reservationRepository.existsOverlapping(roomId, startAt, endAt)).thenReturn(false);
        when(reservationRepository.save(any(ReservationEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateReservationResponse response = handler.execute(request);

        ArgumentCaptor<ReservationEntity> captor = ArgumentCaptor.forClass(ReservationEntity.class);
        verify(reservationRepository).save(captor.capture());

        assertThat(captor.getValue().getStatus()).isEqualTo(ReservationStatus.ACTIVE);
        assertThat(response.roomId()).isEqualTo(roomId);
        assertThat(response.responsible()).isEqualTo("Ana Pérez");
    }

    @Test
    void execute_whenRoomMissing_throwsNotFound() {
        CreateReservationRequest request = new CreateReservationRequest(
                roomId, "Ana Pérez", startAt, endAt
        );
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.execute(request))
                .isInstanceOf(NotFoundException.class);
    }

    private RoomEntity activeRoom() {
        RoomEntity room = new RoomEntity();
        room.setId(roomId);
        room.setName("Sala A");
        room.setLocation("Piso 1");
        room.setCapacity(8);
        room.setActive(true);
        room.setCreatedAt(Instant.now());
        return room;
    }
}
