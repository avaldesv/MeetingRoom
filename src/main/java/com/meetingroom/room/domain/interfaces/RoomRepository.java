package com.meetingroom.room.domain.interfaces;

import com.meetingroom.common.persistence.PageResult;
import com.meetingroom.room.domain.entities.RoomEntity;

import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {

    RoomEntity save(RoomEntity entity);

    Optional<RoomEntity> findById(UUID id);

    PageResult<RoomEntity> search(Boolean active, String name, int page, int size);
}
