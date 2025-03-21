package org.internship.task.rideservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.internship.task.rideservice.enums.Status;

import static org.internship.task.rideservice.util.constantMessages.validationMessages.RideValidationMessages.STATUS_CANNOT_BE_NULL;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusRequest {

    @NotNull(message = STATUS_CANNOT_BE_NULL)
    private Status status;

}
