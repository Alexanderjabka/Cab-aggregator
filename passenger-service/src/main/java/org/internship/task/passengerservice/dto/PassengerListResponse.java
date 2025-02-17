package org.internship.task.passengerservice.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record PassengerListResponse(
        List<PassengerResponse> passengers
) {}
