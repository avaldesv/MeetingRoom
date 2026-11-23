package com.meetingroom.room.application.queries.room.list;

import com.meetingroom.room.domain.entities.RoomEntity;

import java.time.Instant;
import java.util.UUID;

public record ListRoomsResponse(
        UUID id,
        String name,
        String location,
        Integer capacity,
        boolean active,
        Instant createdAt
) {
    public static ListRoomsResponse from(RoomEntity entity) {
        return new ListRoomsResponse(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getCapacity(),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }
}
