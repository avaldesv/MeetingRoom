package com.meetingroom.room.infrastructure.controllers;

import com.meetingroom.room.application.commands.room.create.CreateRoomCommandHandler;
import com.meetingroom.room.application.commands.room.create.CreateRoomRequest;
import com.meetingroom.room.application.commands.room.create.CreateRoomResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final CreateRoomCommandHandler createRoomCommand;

    public RoomController(CreateRoomCommandHandler createRoomCommand) {
        this.createRoomCommand = createRoomCommand;
    }

    @PostMapping
    public ResponseEntity<CreateRoomResponse> create(@Valid @RequestBody CreateRoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createRoomCommand.execute(request));
    }
}