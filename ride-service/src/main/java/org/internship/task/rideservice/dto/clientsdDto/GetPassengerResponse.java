package org.internship.task.rideservice.dto.clientsdDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPassengerResponse {
    @JsonProperty("id")
    private Long passengerId;
}
