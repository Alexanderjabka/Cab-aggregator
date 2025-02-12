package org.internship.task.driverservice.exceptions.carExceptions;

public final class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(String message) {
        super(message);
    }
}
