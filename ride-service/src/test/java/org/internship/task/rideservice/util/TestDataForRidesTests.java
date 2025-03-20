package org.internship.task.rideservice.util;

import org.internship.task.rideservice.dto.RideRequest;
import org.internship.task.rideservice.dto.RideResponse;
import org.internship.task.rideservice.dto.StatusRequest;
import org.internship.task.rideservice.dto.clientsdDto.AssignDriverResponse;
import org.internship.task.rideservice.dto.clientsdDto.GetPassengerResponse;
import org.internship.task.rideservice.entities.Ride;
import org.internship.task.rideservice.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestDataForRidesTests {
    private static final Long ID = 1L;

    private static final Long PASSENGER_ID = 1L;

    private static final Long DRIVER_ID = 1L;

    private static final String START_ADDRESS = "Minsk";

    private static final String FINISH_ADDRESS = "Mogilev";

    private static final Status STATUS = Status.CREATED;

    private static final LocalDateTime ORDER_DATE_TIME = LocalDateTime.now();

    private static final BigDecimal PRICE = null;

    public static StatusRequest createStatusRequest() {
        return new StatusRequest(Status.CREATED);
    }

    public static Ride createRide() {
        return new Ride(ID, PASSENGER_ID, DRIVER_ID, START_ADDRESS, FINISH_ADDRESS, STATUS, ORDER_DATE_TIME, PRICE);
    }

    public static RideResponse createRideResponse() {
        return new RideResponse(ID, PASSENGER_ID, DRIVER_ID, START_ADDRESS, FINISH_ADDRESS, STATUS, ORDER_DATE_TIME,
                PRICE);
    }

    public static RideRequest createRideRequest() {
        return new RideRequest(PASSENGER_ID, START_ADDRESS, FINISH_ADDRESS);
    }

    public static GetPassengerResponse createPassengerResponse() {
        return new GetPassengerResponse(PASSENGER_ID);
    }

    public static AssignDriverResponse createDriverResponse() {
        return new AssignDriverResponse(DRIVER_ID);
    }
}

