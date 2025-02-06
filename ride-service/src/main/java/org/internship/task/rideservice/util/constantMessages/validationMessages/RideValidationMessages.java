package org.internship.task.rideservice.util.constantMessages.validationMessages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RideValidationMessages {
    public static final String PASSENGER_ID_CANNOT_BE_NULL = "Passenger ID cannot be null";
    public static final String DRIVER_ID_CANNOT_BE_NULL = "Driver ID cannot be null";
    public static final String START_ADDRESS_CANNOT_BE_BLANK = "Start address cannot be blank";
    public static final String FINISH_ADDRESS_CANNOT_BE_BLANK = "Finish address cannot be blank";

}
