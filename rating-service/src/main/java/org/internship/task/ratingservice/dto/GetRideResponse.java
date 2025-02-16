package org.internship.task.ratingservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetRideResponse {
    @JsonProperty("id")
    private Long rideId;

    @JsonProperty("driverId")
    private Long driverId;

    @JsonProperty("passengerId")
    private Long passengerId;
}
