package org.internship.task.passengerservice.util;

import lombok.Getter;
import org.internship.task.passengerservice.dto.PassengerRequest;
import org.internship.task.passengerservice.dto.PassengerResponse;
import org.internship.task.passengerservice.entities.Passenger;
@Getter
public class TestDataForPassengersTest {
    private static final Long id = 1L;
    private static final String name = "sasha";
    private static final String email = "sasha@mail.com";
    private static final String phoneNumber = "+375335678990";
    private static final Boolean isDeleted = false;


    public static Passenger createPassenger() {
        return new Passenger(id, name, email, phoneNumber, isDeleted);
    }

    public static PassengerRequest createPassengerRequest() {
        return new PassengerRequest(name, email, phoneNumber);
    }

    public static PassengerResponse createPassengerResponse() {
        return new PassengerResponse(id, name, email, phoneNumber, isDeleted);
    }
}
