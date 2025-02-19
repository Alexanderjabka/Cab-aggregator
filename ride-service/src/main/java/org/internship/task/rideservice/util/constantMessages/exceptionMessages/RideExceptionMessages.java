package org.internship.task.rideservice.util.constantMessages.exceptionMessages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RideExceptionMessages {
    public static final String RIDE_NOT_FOUND_BY_RIDE_ID = "Ride not found with ID: ";
    public static final String RIDE_STATUS_IS_INCORRECT =
        "Ride status is incorrect cause of this status cannot be updated: ";
    public static final String THIS_PASSENGER_ALREADY_HAS_RIDE = "this passenger is already has an active ride";
}
