package org.internship.task.driverservice.dto.drivers;

import java.util.List;
import lombok.Builder;

@Builder
public record DriverListResponse(
        List<DriverResponse> drivers
) {
}
