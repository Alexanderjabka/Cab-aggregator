package org.internship.task.driverservice.dto.cars;

import java.util.List;
import lombok.Builder;

@Builder
public record CarListResponse(
    List<CarResponse> cars
) {
}
