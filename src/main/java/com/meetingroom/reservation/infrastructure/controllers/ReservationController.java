package com.meetingroom.reservation.infrastructure.controllers;

import com.meetingroom.common.dto.HeadersDto;
import com.meetingroom.common.dto.PageResponse;
import com.meetingroom.reservation.application.commands.reservation.cancel.CancelReservationCommandHandler;
import com.meetingroom.reservation.application.commands.reservation.cancel.CancelReservationRequest;
import com.meetingroom.reservation.application.commands.reservation.cancel.CancelReservationResponse;
import com.meetingroom.reservation.application.commands.reservation.create.CreateReservationCommandHandler;
import com.meetingroom.reservation.application.commands.reservation.create.CreateReservationRequest;
import com.meetingroom.reservation.application.commands.reservation.create.CreateReservationResponse;
import com.meetingroom.reservation.application.queries.reservation.list.ListReservationsQueryHandler;
import com.meetingroom.reservation.application.queries.reservation.list.ListReservationsRequest;
import com.meetingroom.reservation.application.queries.reservation.list.ListReservationsResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final CreateReservationCommandHandler createReservationCommand;
    private final CancelReservationCommandHandler cancelReservationCommand;
    private final ListReservationsQueryHandler listReservationsQuery;

    public ReservationController(
            CreateReservationCommandHandler createReservationCommand,
            CancelReservationCommandHandler cancelReservationCommand,
            ListReservationsQueryHandler listReservationsQuery
    ) {
        this.createReservationCommand = createReservationCommand;
        this.cancelReservationCommand = cancelReservationCommand;
        this.listReservationsQuery = listReservationsQuery;
    }

    @PostMapping
    public ResponseEntity<CreateReservationResponse> create(
            @Valid @RequestBody CreateReservationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createReservationCommand.execute(request));
    }

    @PostMapping("/list")
    public ResponseEntity<PageResponse<ListReservationsResponse>> list(
            @RequestBody(required = false) ListReservationsRequest request,
            @RequestHeader(value = "page", required = false) Integer page,
            @RequestHeader(value = "size", required = false) Integer size
    ) {
        HeadersDto headers = new HeadersDto(page, size);
        ListReservationsRequest effectiveRequest = request == null
                ? new ListReservationsRequest(null, null, null, null, null)
                : request;
        return ResponseEntity.ok(listReservationsQuery.execute(effectiveRequest, headers));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CancelReservationResponse> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(cancelReservationCommand.execute(new CancelReservationRequest(id)));
    }
}
