package org.internship.task.ratingservice.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideCompletedEvent {
    private Long rideId;
    private Long passengerId;
    private Long driverId;
}
