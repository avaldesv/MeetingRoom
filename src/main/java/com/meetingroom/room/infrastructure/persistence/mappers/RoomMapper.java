package com.meetingroom.room.infrastructure.persistence.mappers;

import com.meetingroom.room.domain.entities.RoomEntity;
import com.meetingroom.room.infrastructure.persistence.models.RoomModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomEntity toEntity(RoomModel model);

    RoomModel toModel(RoomEntity entity);
}
