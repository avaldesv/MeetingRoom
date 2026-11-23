package com.meetingroom.reservation.infrastructure.persistence.mappers;

import com.meetingroom.reservation.domain.entities.ReservationEntity;
import com.meetingroom.reservation.infrastructure.persistence.models.ReservationModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    ReservationEntity toEntity(ReservationModel model);

    ReservationModel toModel(ReservationEntity entity);
}
