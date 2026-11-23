package com.meetingroom.room.infrastructure.persistence.repositories;

import com.meetingroom.room.infrastructure.persistence.models.RoomModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RoomJpaRepository extends JpaRepository<RoomModel, UUID> {

    @Query("""
            SELECT r FROM RoomModel r
            WHERE (:active IS NULL OR r.active = :active)
              AND (:name IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))
            """)
    Page<RoomModel> search(
            @Param("active") Boolean active,
            @Param("name") String name,
            Pageable pageable
    );
}
