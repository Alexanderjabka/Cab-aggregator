package org.internship.task.passengerservice.controllers;

import static org.internship.task.passengerservice.util.TestDataForPassengersTest.createPassengerRequest;
import static org.internship.task.passengerservice.util.TestDataForPassengersTest.createPassengerResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
        // given
        PassengerListResponse response = new PassengerListResponse(List.of(passengerResponse));
        when(passengerServiceImpl.getAllPassengers()).thenReturn(ResponseEntity.ok(response));

        // when
        ResponseEntity<PassengerListResponse> result = passengerController.getAllPassengers();

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().passengers().size());
        verify(passengerServiceImpl, times(1)).getAllPassengers();
    }

    @Test
    void getAllPassengersByStatus_Success_Status200() {
        // given
        PassengerListResponse response = new PassengerListResponse(List.of(passengerResponse));
        when(passengerServiceImpl.getAllPassengersByStatus(false)).thenReturn(ResponseEntity.ok(response));

        // when
        ResponseEntity<PassengerListResponse> result = passengerController.getAllPassengersByStatus(false);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().passengers().size());
        verify(passengerServiceImpl, times(1)).getAllPassengersByStatus(false);
    }

    @Test
    void getPassengerById_Success_Status200() {
        // given
        when(passengerServiceImpl.getPassengerById(1L)).thenReturn(passengerResponse);

        // when
        ResponseEntity<PassengerResponse> result = passengerController.getPassengerById(1L);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(passengerServiceImpl, times(1)).getPassengerById(1L);
    }

    @Test
    void getPassengerByIdAndStatus_Success_Status200() {
        // given
        when(passengerServiceImpl.getPassengerByIdAndStatus(1L)).thenReturn(passengerResponse);

        // when
        ResponseEntity<PassengerResponse> result = passengerController.getPassengerByIdAndStatus(1L);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(passengerServiceImpl, times(1)).getPassengerByIdAndStatus(1L);
    }

    @Test
    void createPassenger_Success_Status201() {
        // given
        when(passengerServiceImpl.createPassenger(passengerRequest)).thenReturn(passengerResponse);

        // when
        ResponseEntity<PassengerResponse> result = passengerController.createPassenger(passengerRequest);

        // then
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(passengerServiceImpl, times(1)).createPassenger(passengerRequest);
    }

    @Test
    void updatePassenger_Success_Status200() {
        // given
        when(passengerServiceImpl.updatePassenger("zhaba@example.com", passengerRequest)).thenReturn(passengerResponse);

        // when
        ResponseEntity<PassengerResponse> result =
            passengerController.updatePassenger("zhaba@example.com", passengerRequest);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(passengerServiceImpl, times(1)).updatePassenger("zhaba@example.com", passengerRequest);
    }

    @Test
    void deletePassenger_Success_Status204() {
        // given
        doNothing().when(passengerServiceImpl).deletePassenger(1L);

        // when
        ResponseEntity<Void> result = passengerController.deletePassenger(1L);

        // then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(passengerServiceImpl, times(1)).deletePassenger(1L);
    }
}