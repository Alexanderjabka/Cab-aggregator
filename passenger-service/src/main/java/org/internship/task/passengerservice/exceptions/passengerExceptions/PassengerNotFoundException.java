package org.internship.task.passengerservice.exceptions.passengerExceptions;

public class PassengerNotFoundException extends RuntimeException {
    public PassengerNotFoundException(String message) {
        super(message);
    }
}
