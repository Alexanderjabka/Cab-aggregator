package org.internship.task.passengerservice.exceptions.passengerExceptions;

public class InvalidPassengerOperationException extends RuntimeException{
    public InvalidPassengerOperationException(String message) {
        super(message);
    }
}
