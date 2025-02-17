package org.internship.task.rideservice.dto;

import static org.internship.task.rideservice.util.constantMessages.validationMessages.RideValidationMessages.DRIVER_ID_CANNOT_BE_NULL;
import static org.internship.task.rideservice.util.constantMessages.validationMessages.RideValidationMessages.FINISH_ADDRESS_CANNOT_BE_BLANK;
import static org.internship.task.rideservice.util.constantMessages.validationMessages.RideValidationMessages.PASSENGER_ID_CANNOT_BE_NULL;
import static org.internship.task.rideservice.util.constantMessages.validationMessages.RideValidationMessages.START_ADDRESS_CANNOT_BE_BLANK;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
