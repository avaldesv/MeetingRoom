package com.meetingroom.reservation.application.commands.reservation.cancel;

import com.meetingroom.common.application.Command;
import com.meetingroom.common.exception.NotFoundException;
import com.meetingroom.reservation.domain.entities.ReservationEntity;
import com.meetingroom.reservation.domain.interfaces.ReservationRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CancelReservationCommandHandler
        implements Command<CancelReservationRequest, CancelReservationResponse> {

    private final ReservationRepository reservationRepository;

    public CancelReservationCommandHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public CancelReservationResponse execute(CancelReservationRequest request) {
        ReservationEntity reservation = reservationRepository.findById(request.id())
                .orElseThrow(() -> new NotFoundException("Reserva no encontrada: " + request.id()));
        reservation.cancel(Instant.now());
        return CancelReservationResponse.from(reservationRepository.save(reservation));
    }
}
