package org.internship.task.passengerservice.util;

import org.internship.task.passengerservice.dto.PassengerRequest;
import org.internship.task.passengerservice.dto.PassengerResponse;
import org.internship.task.passengerservice.entities.Passenger;

public class TestDataForPassengersTest {
    private static final Long ID = 1L;
    private static final String NAME = "sasha";
    private static final String EMAIL = "sasha@mail.com";
    private static final String PHONE_NUMBER = "+375335678990";
    private static final Boolean IS_DELETED = false;


    public static Passenger createPassenger() {
        return new Passenger(ID, NAME, EMAIL, PHONE_NUMBER, IS_DELETED);
    }

    public static PassengerRequest createPassengerRequest() {
        return new PassengerRequest(NAME, EMAIL, PHONE_NUMBER);
    }

    public static PassengerResponse createPassengerResponse() {
        return new PassengerResponse(ID, NAME, EMAIL, PHONE_NUMBER, IS_DELETED);
    }
}
