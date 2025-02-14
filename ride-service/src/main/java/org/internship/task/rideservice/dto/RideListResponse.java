package org.internship.task.rideservice.dto;

import java.util.List;
import lombok.Builder;


@Builder
public record RideListResponse(
        List<RideResponse> rides
){}
