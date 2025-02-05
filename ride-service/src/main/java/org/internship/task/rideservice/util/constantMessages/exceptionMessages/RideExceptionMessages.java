package org.internship.task.rideservice.util.constantMessages.exceptionMessages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RideExceptionMessages {
    public static final String RIDE_NOT_FOUND_BY_RIDE_ID = "Ride not found with ID: ";
    public static final String RIDE_ALREADY_EXISTS = "A Ride is already exists";
    public static final String RIDE_IS_CANCELLED_OR_COMPLETED = "Ride status is cancelled or completed and cannot be updated: ";

}
