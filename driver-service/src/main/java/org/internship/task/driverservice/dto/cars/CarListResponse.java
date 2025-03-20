package org.internship.task.driverservice.dto.cars;

import lombok.Builder;

import java.util.List;

@Builder
public record CarListResponse(
        List<CarResponse> cars
) {
}
