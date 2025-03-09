package org.internship.task.passengerservice.testDataIT;

import org.internship.task.passengerservice.dto.PassengerRequest;
import org.internship.task.passengerservice.entities.Passenger;

public class DataForIT {
    public static final Long INVALID_ID = 999L;
    public static final String VALID_NAME = "sasha";
    public static final String VALID_EMAIL = "sasha@example.com";
    public static final String VALID_PHONE_NUMBER = "+375297767654";

    public static final String INVALID_NAME = "";
    public static final String INVALID_EMAIL = "sashaexample.com";
    public static final String INVALID_PHONE_NUMBER = "+1234";

    public static final String VALID_NAME_UPDATE = "igor";
    public static final String VALID_EMAIL_UPDATE = "igor@example.com";
    public static final String VALID_PHONE_NUMBER_UPDATE = "+375297878777";

    public static final PassengerRequest CREATE_REQUEST = new PassengerRequest(
        VALID_NAME,
        VALID_EMAIL,
        VALID_PHONE_NUMBER
    );

    public static final PassengerRequest INVALID_CREATE_REQUEST = new PassengerRequest(
        INVALID_NAME,
        INVALID_EMAIL,
        INVALID_PHONE_NUMBER
    );

    public static final PassengerRequest UPDATE_REQUEST = new PassengerRequest(
        VALID_NAME_UPDATE,
        VALID_EMAIL_UPDATE,
        VALID_PHONE_NUMBER_UPDATE
    );

    public static final Passenger PASSENGER = new Passenger(
        1L,
        VALID_NAME,
        VALID_EMAIL,
        VALID_PHONE_NUMBER,
        false
    );
}
