package org.internship.task.passengerservice.exceptions;

public class InvalidPassengerOperationException extends RuntimeException{
    public InvalidPassengerOperationException(String message) {
        super(message);
    }
}
