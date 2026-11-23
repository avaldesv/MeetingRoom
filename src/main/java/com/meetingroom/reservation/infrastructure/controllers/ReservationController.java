package com.meetingroom.reservation.infrastructure.controllers;

import com.meetingroom.reservation.application.commands.reservation.create.CreateReservationCommandHandler;
import com.meetingroom.reservation.application.commands.reservation.create.CreateReservationRequest;
import com.meetingroom.reservation.application.commands.reservation.create.CreateReservationResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final CreateReservationCommandHandler createReservationCommand;

    public ReservationController(CreateReservationCommandHandler createReservationCommand) {
        this.createReservationCommand = createReservationCommand;
    }

    @PostMapping
    public ResponseEntity<CreateReservationResponse> create(
            @Valid @RequestBody CreateReservationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createReservationCommand.execute(request));
    }
}