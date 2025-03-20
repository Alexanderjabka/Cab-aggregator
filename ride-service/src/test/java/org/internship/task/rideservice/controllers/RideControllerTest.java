package org.internship.task.rideservice.controllers;

import org.internship.task.rideservice.dto.RideListResponse;
import org.internship.task.rideservice.dto.RideRequest;
import org.internship.task.rideservice.dto.RideResponse;
import org.internship.task.rideservice.dto.StatusRequest;
import org.internship.task.rideservice.enums.Status;
import org.internship.task.rideservice.services.rideServices.RideServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.internship.task.rideservice.util.TestDataForRidesTests.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RideControllerTest {
    @Mock
    private RideServiceImpl rideServiceImpl;

    @InjectMocks
    private RideController rideController;

    private RideResponse rideResponse;

    private RideRequest rideRequest;
    private StatusRequest statusRequest;

    @BeforeEach
    void setUp() {
        rideResponse = createRideResponse();
        rideRequest = createRideRequest();

        statusRequest = createStatusRequest();
    }

    @Test
    void getAllRides_ShouldReturnListOfRides_WhenRidesExist() {
        RideListResponse expectedResponse = new RideListResponse(List.of(rideResponse));
        when(rideServiceImpl.getAllRides()).thenReturn(ResponseEntity.ok(expectedResponse));

        ResponseEntity<RideListResponse> actualResponse = rideController.getAllRides();

        assertNotNull(actualResponse.getBody());
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedResponse, actualResponse.getBody());
        assertEquals(1, actualResponse.getBody().rides().size());
    }

    @Test
    void getAllRidesByStatus_ShouldReturnFilteredRides_WhenStatusProvided() {
        RideListResponse expectedResponse = new RideListResponse(List.of(rideResponse));
        when(rideServiceImpl.getAllRidesByStatus(Status.CREATED)).thenReturn(ResponseEntity.ok(expectedResponse));

        ResponseEntity<RideListResponse> actualResponse = rideController.getAllRidesByStatus(Status.CREATED);

        assertNotNull(actualResponse.getBody());
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedResponse, actualResponse.getBody());
        assertEquals(1, actualResponse.getBody().rides().size());
    }

    @Test
    void getRideById_ShouldReturnRide_WhenRideExists() {
        when(rideServiceImpl.getRideById(1L)).thenReturn(rideResponse);

        ResponseEntity<RideResponse> actualResponse = rideController.getRideById(1L);

        assertNotNull(actualResponse.getBody());
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(rideResponse, actualResponse.getBody());
    }

    @Test
    void getRideByIdAndAbilityToRate_ShouldReturnRide_WhenRideCanBeRated() {
        when(rideServiceImpl.getRideByIdAndAbilityToRate(1L)).thenReturn(rideResponse);

        ResponseEntity<RideResponse> actualResponse = rideController.getRideByIdAndAbilityToRate(1L);

        assertNotNull(actualResponse.getBody());
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(rideResponse, actualResponse.getBody());
    }

    @Test
    void createRide_ShouldReturnCreatedRide_WhenRequestIsValid() {
        when(rideServiceImpl.createRide(rideRequest)).thenReturn(rideResponse);

        ResponseEntity<RideResponse> actualResponse = rideController.createRide(rideRequest);

        assertNotNull(actualResponse.getBody());
        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertEquals(rideResponse, actualResponse.getBody());
    }

    @Test
    void updateRide_ShouldReturnUpdatedRide_WhenRideExists() {
        when(rideServiceImpl.updateRide(1L, rideRequest)).thenReturn(rideResponse);

        ResponseEntity<RideResponse> actualResponse = rideController.updateRide(1L, rideRequest);

        assertNotNull(actualResponse.getBody());
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(rideResponse, actualResponse.getBody());
    }

    @Test
    void changeStatus_ShouldReturnUpdatedRide_WhenStatusChangesSuccessfully() {
        when(rideServiceImpl.changeStatus(1L, statusRequest)).thenReturn(rideResponse);

        ResponseEntity<RideResponse> actualResponse = rideController.changeStatus(1L, statusRequest);

        assertNotNull(actualResponse.getBody());
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(rideResponse, actualResponse.getBody());
    }
}