package org.internship.task.passengerservice.controllers;

import org.internship.task.passengerservice.dto.PassengerListResponse;
import org.internship.task.passengerservice.dto.PassengerRequest;
import org.internship.task.passengerservice.dto.PassengerResponse;
import org.internship.task.passengerservice.services.PassengerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.internship.task.passengerservice.util.TestDataForPassengersTest.createPassengerRequest;
import static org.internship.task.passengerservice.util.TestDataForPassengersTest.createPassengerResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {

    @Mock
    PassengerServiceImpl passengerServiceImpl;

    @InjectMocks
    PassengerController passengerController;

    private PassengerResponse passengerResponse;
    private PassengerRequest passengerRequest;

    @BeforeEach
    void setUp() {
        passengerRequest = createPassengerRequest();
        passengerResponse = createPassengerResponse();
    }

    @Test
    void getAllPassengers_Success_Status200() {
        PassengerListResponse response = new PassengerListResponse(List.of(passengerResponse));
        when(passengerServiceImpl.getAllPassengers()).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<PassengerListResponse> result = passengerController.getAllPassengers();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().passengers().size());
        verify(passengerServiceImpl).getAllPassengers();
    }

    @Test
    void getAllPassengersByStatus_Success_Status200() {
        PassengerListResponse response = new PassengerListResponse(List.of(passengerResponse));
        when(passengerServiceImpl.getAllPassengersByStatus(false)).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<PassengerListResponse> result = passengerController.getAllPassengersByStatus(false);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().passengers().size());
        verify(passengerServiceImpl).getAllPassengersByStatus(false);
    }

    @Test
    void getPassengerById_Success_Status200() {
        when(passengerServiceImpl.getPassengerById(1L)).thenReturn(passengerResponse);

        ResponseEntity<PassengerResponse> result = passengerController.getPassengerById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(passengerServiceImpl).getPassengerById(1L);
    }

    @Test
    void getPassengerByIdAndStatus_Success_Status200() {
        when(passengerServiceImpl.getPassengerByIdAndStatus(1L)).thenReturn(passengerResponse);

        ResponseEntity<PassengerResponse> result = passengerController.getPassengerByIdAndStatus(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(passengerServiceImpl).getPassengerByIdAndStatus(1L);
    }

    @Test
    void createPassenger_Success_Status201() {
        when(passengerServiceImpl.createPassenger(passengerRequest)).thenReturn(passengerResponse);

        ResponseEntity<PassengerResponse> result = passengerController.createPassenger(passengerRequest);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(passengerServiceImpl).createPassenger(passengerRequest);
    }

    @Test
    void updatePassenger_Success_Status200() {
        when(passengerServiceImpl.updatePassenger("zhaba@example.com", passengerRequest)).thenReturn(passengerResponse);

        ResponseEntity<PassengerResponse> result =
                passengerController.updatePassenger("zhaba@example.com", passengerRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(passengerServiceImpl).updatePassenger("zhaba@example.com", passengerRequest);
    }

    @Test
    void deletePassenger_Success_Status204() {
        doNothing().when(passengerServiceImpl).deletePassenger(1L);

        ResponseEntity<Void> result = passengerController.deletePassenger(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(passengerServiceImpl).deletePassenger(1L);
    }
}