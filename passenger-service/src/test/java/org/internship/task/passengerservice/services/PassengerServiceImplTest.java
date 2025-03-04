package org.internship.task.passengerservice.services;

import static org.internship.task.passengerservice.util.TestDataForPassengersTest.createPassenger;
import static org.internship.task.passengerservice.util.TestDataForPassengersTest.createPassengerRequest;
import static org.internship.task.passengerservice.util.TestDataForPassengersTest.createPassengerResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.internship.task.passengerservice.dto.PassengerListResponse;
import org.internship.task.passengerservice.dto.PassengerRequest;
import org.internship.task.passengerservice.dto.PassengerResponse;
import org.internship.task.passengerservice.entities.Passenger;
import org.internship.task.passengerservice.exceptions.passengerExceptions.InvalidPassengerOperationException;
import org.internship.task.passengerservice.exceptions.passengerExceptions.PassengerNotFoundException;
import org.internship.task.passengerservice.mappers.PassengerMapper;
import org.internship.task.passengerservice.repositories.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class PassengerServiceImplTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerMapper passengerMapper;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    private Passenger passenger;
    private PassengerResponse passengerResponse;
    private PassengerRequest passengerRequest;

    @BeforeEach
    void setUp() {
        passenger = createPassenger();
        passengerRequest = createPassengerRequest();
        passengerResponse = createPassengerResponse();
    }

    @Test
    void getAllPassengers_Success_Status200() {
        when(passengerRepository.findAllByOrderByIdAsc()).thenReturn(List.of(passenger));
        when(passengerMapper.toDtoList(List.of(passenger))).thenReturn(List.of(passengerResponse));

        ResponseEntity<PassengerListResponse> result = passengerService.getAllPassengers();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().passengers().size());
        verify(passengerRepository).findAllByOrderByIdAsc();
    }

    @Test
    void getAllPassengers_NoContent_Status204() {
        when(passengerRepository.findAllByOrderByIdAsc()).thenReturn(Collections.emptyList());

        ResponseEntity<PassengerListResponse> result = passengerService.getAllPassengers();

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(passengerRepository).findAllByOrderByIdAsc();
    }

    @Test
    void getAllPassengersByStatus_Success_Status200() {
        when(passengerRepository.findByIsDeletedOrderByIdAsc(false)).thenReturn(List.of(passenger));
        when(passengerMapper.toDtoList(List.of(passenger))).thenReturn(List.of(passengerResponse));

        ResponseEntity<PassengerListResponse> result = passengerService.getAllPassengersByStatus(false);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().passengers().size());
        verify(passengerRepository).findByIsDeletedOrderByIdAsc(false);
    }

    @Test
    void getPassengerById_Success() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(passengerMapper.toDto(passenger)).thenReturn(passengerResponse);

        PassengerResponse result = passengerService.getPassengerById(1L);

        assertNotNull(result);
        assertEquals("sasha", result.getName());
        assertEquals("sasha@mail.com", result.getEmail());
        verify(passengerRepository).findById(1L);
    }

    @Test
    void getPassengerById_NotFound() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PassengerNotFoundException.class, () -> passengerService.getPassengerById(1L));
        verify(passengerRepository).findById(1L);
    }

    @Test
    void getPassengerByIdAndStatus_Success() {
        when(passengerRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(passenger));
        when(passengerMapper.toDto(passenger)).thenReturn(passengerResponse);

        PassengerResponse result = passengerService.getPassengerByIdAndStatus(1L);

        assertNotNull(result);
        assertEquals("sasha", result.getName());
        assertEquals("sasha@mail.com", result.getEmail());
        verify(passengerRepository).findByIdAndIsDeletedFalse(1L);
    }

    @Test
    void getPassengerByIdAndStatus_NotFound() {
        when(passengerRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.empty());

        assertThrows(PassengerNotFoundException.class, () -> passengerService.getPassengerByIdAndStatus(1L));
        verify(passengerRepository).findByIdAndIsDeletedFalse(1L);
    }

    @Test
    void createPassenger_Success() {
        when(passengerRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.empty());
        when(passengerMapper.toEntity(passengerRequest)).thenReturn(passenger);
        when(passengerMapper.toDto(passenger)).thenReturn(passengerResponse);

        PassengerResponse result = passengerService.createPassenger(passengerRequest);

        assertNotNull(result);
        assertEquals("sasha", result.getName());
        assertEquals("sasha@mail.com", result.getEmail());
        verify(passengerRepository).save(passenger);
    }

    @Test
    void createPassenger_AlreadyExists() {
        when(passengerRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.of(new Passenger()));

        assertThrows(InvalidPassengerOperationException.class,
            () -> passengerService.createPassenger(passengerRequest));
        verify(passengerRepository).findByEmail("sasha@mail.com");
    }

    @Test
    void deletePassenger_Success() {
        when(passengerRepository.existsById(1L)).thenReturn(true);
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        assertFalse(passenger.getIsDeleted());
        passengerService.deletePassenger(1L);

        assertTrue(passenger.getIsDeleted());
        verify(passengerRepository).save(passenger);
    }

    @Test
    void deletePassenger_NotFound() {
        when(passengerRepository.existsById(1L)).thenReturn(false);

        assertThrows(PassengerNotFoundException.class, () -> passengerService.deletePassenger(1L));
        verify(passengerRepository).existsById(1L);
    }

    @Test
    void updatePassenger_Success() {
        when(passengerRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.of(passenger));
        when(passengerMapper.toDto(passenger)).thenReturn(passengerResponse);

        PassengerResponse result = passengerService.updatePassenger("sasha@mail.com", passengerRequest);

        assertNotNull(result);
        assertEquals("sasha", result.getName());
        assertEquals("sasha@mail.com", result.getEmail());
        verify(passengerRepository).save(passenger);
    }

    @Test
    void updatePassenger_NotFound() {
        when(passengerRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.empty());

        assertThrows(PassengerNotFoundException.class,
            () -> passengerService.updatePassenger("sasha@mail.com", passengerRequest));
        verify(passengerRepository).findByEmail("sasha@mail.com");
    }

    @Test
    void updatePassenger_AlreadyExists() {
        when(passengerRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.of(passenger));
        when(passengerRepository.findByEmail("new@mail.com")).thenReturn(Optional.of(new Passenger()));

        passengerRequest.setEmail("new@mail.com");

        assertThrows(InvalidPassengerOperationException.class, () ->
            passengerService.updatePassenger("sasha@mail.com", passengerRequest)
        );

        verify(passengerRepository).findByEmail("sasha@mail.com");
        verify(passengerRepository).findByEmail("new@mail.com");
    }
}