package org.internship.task.driverservice.util.constantMessages.validationMessages;

public final class DriverValidationMessages {
    public static final String NAME_NOT_EMPTY = "name cannot be empty";
    public static final String NAME_SIZE = "name must be between 2 and 30 characters";
    public static final String EMAIL_INVALID = "invalid email format";
    public static final String EMAIL_NOT_EMPTY = "email cannot be empty";
    public static final String PHONE_NUMBER_INVALID = "phone number must match +375 format";
    public static final String PHONE_NUMBER_SIZE = "phone number must be between 10 and 15 characters";
    public static final String PHONE_NUMBER_NOT_EMPTY = "phone number cannot be empty";
    public static final String GENDER_NOT_EMPTY = "gender cannot be empty";
    private DriverValidationMessages(){
    }
}
