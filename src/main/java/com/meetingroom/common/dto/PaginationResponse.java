package com.meetingroom.common.dto;

public record PaginationResponse(
        int page,
        int pageSize,
        long total
) {
}
