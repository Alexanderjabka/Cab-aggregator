package org.internship.task.rideservice.util.constantMessages.exceptionMessages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RideExceptionMessages {
    public static final String RIDE_NOT_FOUND_BY_RIDE_ID = "Driver not found with ID: ";
    public static final String RIDE_ALREADY_EXISTS = "A Driver with the same Email already exists: ";
    public static final String RIDE_IS_CANCELLED_OR_COMPLETED = "Driver is deleted and cannot be updated: ";
}
