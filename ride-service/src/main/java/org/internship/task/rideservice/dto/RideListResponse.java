package org.internship.task.rideservice.dto;

import lombok.Builder;

import java.util.List;


@Builder
public record RideListResponse(
        List<RideResponse> rides
) {
}
