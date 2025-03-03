package org.internship.task.rideservice.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.internship.task.rideservice.dto.RideRequest;
import org.internship.task.rideservice.dto.RideResponse;
import org.internship.task.rideservice.dto.StatusRequest;
import org.internship.task.rideservice.dto.clientsdDto.AssignDriverResponse;
import org.internship.task.rideservice.dto.clientsdDto.GetPassengerResponse;
import org.internship.task.rideservice.entities.Ride;
import org.internship.task.rideservice.enums.Status;

public class TestDataForRidesTests {
    private static final Long id = 1L;

    private static final Long passengerId = 1L;

    private static final Long driverId = 1L;

    private static final String startAddress = "Minsk";

    private static final String finishAddress = "Mogilev";

    private static final Status status = Status.CREATED;

    private static final LocalDateTime orderDateTime = LocalDateTime.now();

    private static final BigDecimal price = null;

    public static StatusRequest createStatusRequest() {
        return new StatusRequest(Status.CREATED);
    }
    public static Ride createRide() {
        return new Ride(id, passengerId, driverId, startAddress, finishAddress, status, orderDateTime, price);
    }

    public static RideResponse createRideResponse() {
        return new RideResponse(id, passengerId, driverId, startAddress, finishAddress, status, orderDateTime, price);
    }

    public static RideRequest createRideRequest() {
        return new RideRequest(passengerId, startAddress, finishAddress);
    }

    public static GetPassengerResponse createPassengerResponse() {
        return new GetPassengerResponse(passengerId);
    }
    public static AssignDriverResponse createDriverResponse() {
        return new AssignDriverResponse(driverId);
    }
}

