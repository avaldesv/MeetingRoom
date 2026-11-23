package com.meetingroom.room.application.queries.room.list;

import java.util.List;

public record ListRoomsRequest(
        Boolean active,
        String name,
        List<String> filters,
        List<String> ordersBy
) {
}
