package com.meetingroom.room.application.commands.room.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateRoomRequest(
        @NotBlank(message = "name es obligatorio")
        @Size(max = 120, message = "name no puede superar 120 caracteres")
        String name,
        @Size(max = 255, message = "location no puede superar 255 caracteres")
        String location,
        @NotNull(message = "capacity es obligatorio")
        @Min(value = 1, message = "capacity debe ser mayor a 0")
        Integer capacity
) {
}
