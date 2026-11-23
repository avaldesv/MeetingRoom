package com.meetingroom.common.application;

public interface Command<REQ, RES> {
    RES execute(REQ request);
}
