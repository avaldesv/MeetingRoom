package com.meetingroom.reservation.application.queries.reservation.list;

import com.meetingroom.common.application.Query;
import com.meetingroom.common.dto.HeadersDto;
import com.meetingroom.common.dto.PageResponse;
import com.meetingroom.common.persistence.PageResult;
import com.meetingroom.reservation.domain.entities.ReservationEntity;
import com.meetingroom.reservation.domain.interfaces.ReservationRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
public class ListReservationsQueryHandler
        implements Query<ListReservationsRequest, PageResponse<ListReservationsResponse>> {

    private final ReservationRepository reservationRepository;

    public ListReservationsQueryHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public PageResponse<ListReservationsResponse> execute(ListReservationsRequest request, HeadersDto headers) {
        Instant dateFrom = null;
        Instant dateTo = null;
        LocalDate date = request == null ? null : request.date();
        if (date != null) {
            dateFrom = date.atStartOfDay().toInstant(ZoneOffset.UTC);
            dateTo = date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        }

        PageResult<ReservationEntity> result = reservationRepository.search(
                request == null ? null : request.roomId(),
                dateFrom,
                dateTo,
                request == null ? null : request.status(),
                headers.pageOrDefault(),
                headers.sizeOrDefault()
        );

        return PageResponse.of(
                result.items().stream().map(ListReservationsResponse::from).toList(),
                result.page(),
                result.pageSize(),
                result.total()
        );
    }
}
