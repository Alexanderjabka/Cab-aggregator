package org.internship.task.rideservice.testDataIT;

import org.internship.task.rideservice.entities.Ride;
import org.internship.task.rideservice.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DataForIT {

    public static final long INVALID_ID = 999L;
    public static final long ID = 1L;
    public static final Long PASSENGER_ID = 1L;
    public static final Long DRIVER_ID = 1L;

    public static final String START_ADDRESS = "Mogilev";
    public static final String FINISH_ADDRESS = "Minsk";
    public static final String NEW_START_ADDRESS = "Grodno";
    public static final String NEW_FINISH_ADDRESS = "Gomel";

    public static final BigDecimal PRICE = BigDecimal.valueOf(10.0);
    public static final LocalDateTime ORDER_DATE_TIME = LocalDateTime.now();

    public static final String PASSENGER_HAS_ACTIVE_RIDE_MESSAGE = "this passenger is already has an active ride";
    public static final String RIDE_NOT_FOUND_MESSAGE = "Ride not found with ID: ";
    public static final String RIDE_STATUS_INCORRECT_MESSAGE =
            "Ride status is incorrect cause of this status cannot be updated: ";

    public static final String CREATE_RIDE_JSON =
            "{\"passengerId\": 1, \"startAddress\": \"Minsk\", \"finishAddress\": \"Mogilev\"}";
    public static final String UPDATE_RIDE_JSON =
            "{\"passengerId\": 1, \"startAddress\": \"Grodno\", \"finishAddress\": \"Gomel\"}";
    public static final String CHANGE_STATUS_JSON = "{\"status\": \"EN_ROUTE_TO_PASSENGER\"}";

    public static final String STATUS_CREATED = "CREATED";
    public static final String STATUS_EN_ROUTE_TO_PASSENGER = "EN_ROUTE_TO_PASSENGER";
    public static final String STATUS_COMPLETED = "COMPLETED";

    // Предопределенная поездка
    public static final Ride CREATE_RIDE = new Ride(
            ID,
            PASSENGER_ID,
            DRIVER_ID,
            START_ADDRESS,
            FINISH_ADDRESS,
            Status.CREATED,
            ORDER_DATE_TIME,
            PRICE
    );

    public static final String WIREMOCK_API_KEY = "test-api-key";
    public static final double START_LATITUDE = 27.56667;
    public static final double START_LONGITUDE = 53.9;
    public static final double FINISH_LATITUDE = 30.283083;
    public static final double FINISH_LONGITUDE = 53.769428;
}