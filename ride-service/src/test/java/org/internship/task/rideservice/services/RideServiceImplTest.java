package org.internship.task.rideservice.services;

import org.internship.task.rideservice.clients.DriverClient;
import org.internship.task.rideservice.clients.PassengerClient;
import org.internship.task.rideservice.dto.RideListResponse;
import org.internship.task.rideservice.dto.RideRequest;
import org.internship.task.rideservice.dto.RideResponse;
import org.internship.task.rideservice.dto.StatusRequest;
import org.internship.task.rideservice.dto.clientsdDto.AssignDriverResponse;
import org.internship.task.rideservice.dto.clientsdDto.GetPassengerResponse;
import org.internship.task.rideservice.entities.Ride;
import org.internship.task.rideservice.enums.Status;
import org.internship.task.rideservice.exceptions.rideExceptions.InvalidRideOperationException;
import org.internship.task.rideservice.exceptions.rideExceptions.RideNotFoundException;
import org.internship.task.rideservice.mappers.RideMapper;
import org.internship.task.rideservice.repositories.RideRepository;
import org.internship.task.rideservice.services.mapServices.MapService;
import org.internship.task.rideservice.services.rideServices.RideServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.internship.task.rideservice.util.TestDataForRidesTests.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceImplTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private MapService mapService;

    @Mock
    private RideMapper rideMapper;

    @Mock
    private DriverClient driverClient;

    @Mock
    private PassengerClient passengerClient;

    @InjectMocks
    private RideServiceImpl rideService;

    private Ride ride;
    private RideResponse rideResponse;
    private RideRequest rideRequest;
    private StatusRequest statusRequest;
    private AssignDriverResponse driverResponse;
    private GetPassengerResponse passengerResponse;

    @BeforeEach
    void setUp() {
        ride = createRide();
        rideRequest = createRideRequest();
        rideResponse = createRideResponse();

        statusRequest = createStatusRequest();

        driverResponse = createDriverResponse();
        passengerResponse = createPassengerResponse();
    }

    @Test
    void getAllRides_ShouldReturnNoContentWhenNoRidesExist() {
        when(rideRepository.findAllByOrderByIdAsc()).thenReturn(Collections.emptyList());

        ResponseEntity<RideListResponse> response = rideService.getAllRides();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(rideRepository).findAllByOrderByIdAsc();
    }

    @Test
    void getAllRides_ShouldReturnRidesWhenRidesExist() {
        when(rideRepository.findAllByOrderByIdAsc()).thenReturn(List.of(ride));
        when(rideMapper.toDtoList(List.of(ride))).thenReturn(List.of(rideResponse));

        ResponseEntity<RideListResponse> response = rideService.getAllRides();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().rides().size());
        verify(rideRepository).findAllByOrderByIdAsc();
    }

    @Test
    void getAllRidesByStatus_ShouldReturnNoContentWhenNoRidesExist() {
        when(rideRepository.findAllByStatusOrderByIdAsc(Status.CREATED)).thenReturn(Collections.emptyList());

        ResponseEntity<RideListResponse> response = rideService.getAllRidesByStatus(Status.CREATED);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(rideRepository).findAllByStatusOrderByIdAsc(Status.CREATED);
    }

    @Test
    void getAllRidesByStatus_ShouldReturnRidesWhenRidesExist() {
        when(rideRepository.findAllByStatusOrderByIdAsc(Status.CREATED)).thenReturn(List.of(ride));
        when(rideMapper.toDtoList(List.of(ride))).thenReturn(List.of(rideResponse));

        ResponseEntity<RideListResponse> response = rideService.getAllRidesByStatus(Status.CREATED);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().rides().size());
        verify(rideRepository).findAllByStatusOrderByIdAsc(Status.CREATED);
    }

    @Test
    void getRideById_ShouldReturnRideWhenRideExists() {
        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));
        when(rideMapper.toDto(ride)).thenReturn(rideResponse);

        RideResponse response = rideService.getRideById(1L);

        assertNotNull(response);
        assertEquals(rideResponse, response);
        verify(rideRepository).findById(1L);
    }

    @Test
    void getRideById_ShouldThrowExceptionWhenRideDoesNotExist() {
        when(rideRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RideNotFoundException.class, () -> rideService.getRideById(1L));
        verify(rideRepository).findById(1L);
    }

    @Test
    void getRideByIdAndAbilityToRate_ShouldReturnRideWhenStatusIsCompletedOrCancelled() {
        ride.setStatus(Status.COMPLETED);
        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));
        when(rideMapper.toDto(ride)).thenReturn(rideResponse);

        RideResponse response = rideService.getRideByIdAndAbilityToRate(1L);

        assertNotNull(response);
        assertEquals(rideResponse, response);
        verify(rideRepository).findById(1L);
    }

    @Test
    void getRideByIdAndAbilityToRate_ShouldThrowExceptionWhenStatusIsNotCompletedOrCancelled() {
        ride.setStatus(Status.CREATED);
        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));

        assertThrows(InvalidRideOperationException.class, () -> rideService.getRideByIdAndAbilityToRate(1L));
        verify(rideRepository).findById(1L);
    }

    @Test
    void createRide_ShouldCreateRideWhenPassengerDoesNotHaveActiveRides() {
        when(passengerClient.getPassengerByIdAndStatus(1L)).thenReturn(passengerResponse);
        when(rideRepository.existsByPassengerIdAndStatusIn(1L, Status.getActiveStatuses())).thenReturn(false);
        when(driverClient.assignDriver()).thenReturn(driverResponse);
        when(mapService.getDistance("Minsk", "Mogilev")).thenReturn(100.0);
        when(rideMapper.toEntity(rideRequest)).thenReturn(ride);
        when(rideMapper.toDto(any(Ride.class))).thenReturn(rideResponse);

        RideResponse response = rideService.createRide(rideRequest);

        assertNotNull(response);
        assertEquals(rideResponse, response);
        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    void createRide_ShouldThrowExceptionWhenPassengerHasActiveRides() {
        when(passengerClient.getPassengerByIdAndStatus(1L)).thenReturn(passengerResponse);
        when(rideRepository.existsByPassengerIdAndStatusIn(1L, Status.getActiveStatuses())).thenReturn(true);

        assertThrows(InvalidRideOperationException.class, () -> rideService.createRide(rideRequest));
    }

    @Test
    void updateRide_ShouldUpdateRideWhenStatusIsNotCompletedOrCancelled() {
        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));
        when(mapService.getDistance("Minsk", "Mogilev")).thenReturn(100.0);
        when(rideMapper.toDto(ride)).thenReturn(rideResponse);

        RideResponse response = rideService.updateRide(1L, rideRequest);

        assertNotNull(response);
        assertEquals(rideResponse, response);
        verify(rideRepository).save(ride);
    }

    @Test
    void updateRide_ShouldThrowExceptionWhenStatusIsCompletedOrCancelled() {
        ride.setStatus(Status.COMPLETED);

        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));

        assertThrows(InvalidRideOperationException.class, () -> rideService.updateRide(1L, rideRequest));
    }

    @Test
    void changeStatus_ShouldChangeStatusWhenStatusIsNotCompletedOrCancelled() {
        statusRequest.setStatus(Status.COMPLETED);

        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));
        when(rideMapper.toDto(ride)).thenReturn(rideResponse);

        RideResponse response = rideService.changeStatus(1L, statusRequest);

        assertNotNull(response);
        assertEquals(rideResponse, response);
        verify(rideRepository).save(ride);
        verify(driverClient).releaseDriver(1L);
    }

    @Test
    void changeStatus_ShouldThrowExceptionWhenStatusIsCompletedOrCancelled() {
        statusRequest.setStatus(Status.COMPLETED);
        ride.setStatus(Status.COMPLETED);

        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));

        assertThrows(InvalidRideOperationException.class, () -> rideService.changeStatus(1L, statusRequest));
    }
}