package com.meetingroom.room.infrastructure.controllers;

import com.meetingroom.common.dto.HeadersDto;
import com.meetingroom.common.dto.PageResponse;
import com.meetingroom.room.application.commands.room.create.CreateRoomCommandHandler;
import com.meetingroom.room.application.commands.room.create.CreateRoomRequest;
import com.meetingroom.room.application.commands.room.create.CreateRoomResponse;
import com.meetingroom.room.application.queries.room.list.ListRoomsQueryHandler;
import com.meetingroom.room.application.queries.room.list.ListRoomsRequest;
import com.meetingroom.room.application.queries.room.list.ListRoomsResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final CreateRoomCommandHandler createRoomCommand;
    private final ListRoomsQueryHandler listRoomsQuery;

    public RoomController(
            CreateRoomCommandHandler createRoomCommand,
            ListRoomsQueryHandler listRoomsQuery
    ) {
        this.createRoomCommand = createRoomCommand;
        this.listRoomsQuery = listRoomsQuery;
    }

    @PostMapping
    public ResponseEntity<CreateRoomResponse> create(@Valid @RequestBody CreateRoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createRoomCommand.execute(request));
    }

    @PostMapping("/list")
    public ResponseEntity<PageResponse<ListRoomsResponse>> list(
            @RequestBody(required = false) ListRoomsRequest request,
            @RequestHeader(value = "page", required = false) Integer page,
            @RequestHeader(value = "size", required = false) Integer size
    ) {
        HeadersDto headers = new HeadersDto(page, size);
        ListRoomsRequest effectiveRequest = request == null
                ? new ListRoomsRequest(null, null, null, null)
                : request;
        return ResponseEntity.ok(listRoomsQuery.execute(effectiveRequest, headers));
    }
}
