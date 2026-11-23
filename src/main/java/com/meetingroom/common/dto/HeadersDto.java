package com.meetingroom.common.dto;

public record HeadersDto(
        Integer page,
        Integer size
) {
    public int pageOrDefault() {
        return page == null ? 0 : Math.max(page, 0);
    }

    public int sizeOrDefault() {
        if (size == null) {
            return 20;
        }
        if (size < 1) {
            return 1;
        }
        return Math.min(size, 2000);
    }
}
