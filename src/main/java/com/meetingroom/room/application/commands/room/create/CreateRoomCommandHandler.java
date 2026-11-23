package com.meetingroom.room.application.commands.room.create;

import com.meetingroom.common.application.Command;
import com.meetingroom.room.domain.entities.RoomEntity;
import com.meetingroom.room.domain.interfaces.RoomRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class CreateRoomCommandHandler implements Command<CreateRoomRequest, CreateRoomResponse> {

    private final RoomRepository roomRepository;

    public CreateRoomCommandHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public CreateRoomResponse execute(CreateRoomRequest request) {
        RoomEntity entity = new RoomEntity();
        entity.setId(UUID.randomUUID());
        entity.setName(request.name().trim());
        entity.setLocation(request.location() == null ? null : request.location().trim());
        entity.setCapacity(request.capacity());
        entity.setActive(true);
        entity.setCreatedAt(Instant.now());
        return CreateRoomResponse.from(roomRepository.save(entity));
    }
}
