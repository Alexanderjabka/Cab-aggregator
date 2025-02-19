package org.internship.task.driverservice.util.constantMessages.exceptionMessages;

public final class DriverExceptionMessages {
    public static final String DRIVER_NOT_FOUND_BY_ID = "Driver not found with ID: ";
    public static final String DRIVER_NOT_FOUND_BY_EMAIL = "Driver not found with Email: ";
    public static final String DRIVER_ALREADY_EXISTS = "A Driver with the same Email already exists: ";
    public static final String DRIVER_IS_DELETED = "Driver is deleted and cannot be updated: ";
    public static final String THERE_ARE_NO_FREE_DRIVERS = "There are no free drivers now ";
    public static final String DRIVER_WITH_THIS_ID_IS_ALREADY_FREE = "Driver with this ID is already free ";

    private DriverExceptionMessages() {
    }
}
