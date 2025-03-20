package org.internship.task.passengerservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PassengerListResponse(
        List<PassengerResponse> passengers
) {
}
