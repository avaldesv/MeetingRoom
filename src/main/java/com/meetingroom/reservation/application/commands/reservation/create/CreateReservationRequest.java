package com.meetingroom.reservation.application.commands.reservation.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record CreateReservationRequest(
        @NotNull(message = "roomId es obligatorio")
        UUID roomId,
        @NotBlank(message = "responsible es obligatorio")
        @Size(max = 120, message = "responsible no puede superar 120 caracteres")
        String responsible,
        @NotNull(message = "startAt es obligatorio")
        Instant startAt,
        @NotNull(message = "endAt es obligatorio")
        Instant endAt
) {
}
