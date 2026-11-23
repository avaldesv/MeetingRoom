package com.meetingroom.room.infrastructure.persistence.repositories;

import com.meetingroom.common.persistence.PageResult;
import com.meetingroom.room.domain.entities.RoomEntity;
import com.meetingroom.room.domain.interfaces.RoomRepository;
import com.meetingroom.room.infrastructure.persistence.mappers.RoomMapper;
import com.meetingroom.room.infrastructure.persistence.models.RoomModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RoomRepositoryImpl implements RoomRepository {

    private final RoomJpaRepository roomJpaRepository;
    private final RoomMapper roomMapper;

    public RoomRepositoryImpl(RoomJpaRepository roomJpaRepository, RoomMapper roomMapper) {
        this.roomJpaRepository = roomJpaRepository;
        this.roomMapper = roomMapper;
    }

    @Override
    public RoomEntity save(RoomEntity entity) {
        RoomModel saved = roomJpaRepository.save(roomMapper.toModel(entity));
        return roomMapper.toEntity(saved);
    }

    @Override
    public Optional<RoomEntity> findById(UUID id) {
        return roomJpaRepository.findById(id).map(roomMapper::toEntity);
    }

    @Override
    public PageResult<RoomEntity> search(Boolean active, String name, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<RoomModel> result = roomJpaRepository.search(active, name, pageable);
        return new PageResult<>(
                result.getContent().stream().map(roomMapper::toEntity).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements()
        );
    }
}
