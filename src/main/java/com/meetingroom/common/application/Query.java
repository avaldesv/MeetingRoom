package com.meetingroom.common.application;

import com.meetingroom.common.dto.HeadersDto;

public interface Query<REQ, RES> {
    RES execute(REQ request, HeadersDto headers);
}
