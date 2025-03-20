package org.internship.task.driverservice.dto.drivers;

import lombok.Builder;

import java.util.List;

@Builder
public record DriverListResponse(
        List<DriverResponse> drivers
) {
}
