package org.internship.task.rideservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.internship.task.rideservice.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RideRequest {
    private Long passengerId;
    private Long driverId;
    private String startAddress;
    private String finishAddress;
}
