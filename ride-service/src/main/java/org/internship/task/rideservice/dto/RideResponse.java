package org.internship.task.rideservice.dto;

import lombok.*;
import org.internship.task.rideservice.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideResponse {

    private Long id;
    private Long passengerId;
    private Long driverId;
    private String startAddress;
    private String finishAddress;
    private Status status;
    private LocalDateTime orderDateTime;
    private BigDecimal price;

}
