package org.internship.task.rideservice.exceptions.rideExceptions;

public class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(String message) {
        super(message);
    }
}
