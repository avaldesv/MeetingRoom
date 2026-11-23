package com.meetingroom.reservation.domain.entities;

import com.meetingroom.common.domain.BaseEntity;
import com.meetingroom.common.exception.BadRequestException;

import java.time.Instant;
import java.util.UUID;

public class ReservationEntity extends BaseEntity {

    private UUID roomId;
    private String responsible;
    private Instant startAt;
    private Instant endAt;
    private ReservationStatus status;
    private Instant cancelledAt;

    public void cancel(Instant cancelledAt) {
        if (status == ReservationStatus.CANCELLED) {
            throw new BadRequestException("La reserva ya está cancelada");
        }
        this.status = ReservationStatus.CANCELLED;
        this.cancelledAt = cancelledAt;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getEndAt() {
        return endAt;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}
