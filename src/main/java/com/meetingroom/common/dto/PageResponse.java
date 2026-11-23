package com.meetingroom.common.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> data,
        PaginationResponse pagination
) {
    public static <T> PageResponse<T> of(List<T> data, int page, int pageSize, long total) {
        return new PageResponse<>(data, new PaginationResponse(page, pageSize, total));
    }
}
