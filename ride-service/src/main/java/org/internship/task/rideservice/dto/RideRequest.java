package org.internship.task.rideservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static org.internship.task.rideservice.util.constantMessages.validationMessages.RideValidationMessages.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RideRequest {

    @NotNull(message = PASSENGER_ID_CANNOT_BE_NULL)
    private Long passengerId;

    @NotBlank(message = START_ADDRESS_CANNOT_BE_BLANK)
    private String startAddress;

    @NotBlank(message = FINISH_ADDRESS_CANNOT_BE_BLANK)
    private String finishAddress;

}
