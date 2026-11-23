package com.meetingroom.room.application.commands.room.create;

import com.meetingroom.room.domain.entities.RoomEntity;

import java.time.Instant;
import java.util.UUID;

public record CreateRoomResponse(
        UUID id,
        String name,
        String location,
        Integer capacity,
        boolean active,
        Instant createdAt
) {
    public static CreateRoomResponse from(RoomEntity entity) {
        return new CreateRoomResponse(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getCapacity(),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }
}
