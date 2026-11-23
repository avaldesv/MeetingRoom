package com.meetingroom.room.application.queries.room.list;

import com.meetingroom.common.application.Query;
import com.meetingroom.common.dto.HeadersDto;
import com.meetingroom.common.dto.PageResponse;
import com.meetingroom.common.persistence.PageResult;
import com.meetingroom.room.domain.entities.RoomEntity;
import com.meetingroom.room.domain.interfaces.RoomRepository;
import org.springframework.stereotype.Component;

@Component
public class ListRoomsQueryHandler implements Query<ListRoomsRequest, PageResponse<ListRoomsResponse>> {

    private final RoomRepository roomRepository;

    public ListRoomsQueryHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public PageResponse<ListRoomsResponse> execute(ListRoomsRequest request, HeadersDto headers) {
        String name = blankToNull(request == null ? null : request.name());
        Boolean active = request == null ? null : request.active();

        PageResult<RoomEntity> result = roomRepository.search(
                active,
                name,
                headers.pageOrDefault(),
                headers.sizeOrDefault()
        );

        return PageResponse.of(
                result.items().stream().map(ListRoomsResponse::from).toList(),
                result.page(),
                result.pageSize(),
                result.total()
        );
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
