package org.internship.task.rideservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.internship.task.rideservice.enums.Status;

@Getter
@Setter
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
