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
import static org.mockito.Mockito.times;
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
        // given
        when(passengerRepository.findAllByOrderByIdAsc()).thenReturn(List.of(passenger));
        when(passengerMapper.toDtoList(List.of(passenger))).thenReturn(List.of(passengerResponse));

        // when
        ResponseEntity<PassengerListResponse> result = passengerService.getAllPassengers();

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().passengers().size());
        verify(passengerRepository, times(1)).findAllByOrderByIdAsc();
    }

    @Test
    void getAllPassengers_NoContent_Status204() {
        // given
        when(passengerRepository.findAllByOrderByIdAsc()).thenReturn(Collections.emptyList());

        // when
        ResponseEntity<PassengerListResponse> result = passengerService.getAllPassengers();

        // then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(passengerRepository, times(1)).findAllByOrderByIdAsc();
    }

    @Test
    void getAllPassengersByStatus_Success_Status200() {
        // given
        when(passengerRepository.findByIsDeletedOrderByIdAsc(false)).thenReturn(List.of(passenger));
        when(passengerMapper.toDtoList(List.of(passenger))).thenReturn(List.of(passengerResponse));

        // when
        ResponseEntity<PassengerListResponse> result = passengerService.getAllPassengersByStatus(false);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().passengers().size());
        verify(passengerRepository, times(1)).findByIsDeletedOrderByIdAsc(false);
    }

    @Test
    void getPassengerById_Success() {
        // given
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(passengerMapper.toDto(passenger)).thenReturn(passengerResponse);

        // when
        PassengerResponse result = passengerService.getPassengerById(1L);

        // then
        assertNotNull(result);
        assertEquals("sasha", result.getName());
        assertEquals("sasha@mail.com", result.getEmail());
        verify(passengerRepository, times(1)).findById(1L);
    }

    @Test
    void getPassengerById_NotFound() {
        // given
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        // when / then
        assertThrows(PassengerNotFoundException.class, () -> passengerService.getPassengerById(1L));
        verify(passengerRepository, times(1)).findById(1L);
    }

    @Test
    void getPassengerByIdAndStatus_Success() {
        // given
        when(passengerRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(passenger));
        when(passengerMapper.toDto(passenger)).thenReturn(passengerResponse);

        // when
        PassengerResponse result = passengerService.getPassengerByIdAndStatus(1L);

        // then
        assertNotNull(result);
        assertEquals("sasha", result.getName());
        assertEquals("sasha@mail.com", result.getEmail());
        verify(passengerRepository, times(1)).findByIdAndIsDeletedFalse(1L);
    }

    @Test
    void getPassengerByIdAndStatus_NotFound() {
        // given
        when(passengerRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.empty());

        // when / then
        assertThrows(PassengerNotFoundException.class, () -> passengerService.getPassengerByIdAndStatus(1L));
        verify(passengerRepository, times(1)).findByIdAndIsDeletedFalse(1L);
    }

    @Test
    void createPassenger_Success() {
        // given
        when(passengerRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.empty());
        when(passengerMapper.toEntity(passengerRequest)).thenReturn(passenger);
        when(passengerMapper.toDto(passenger)).thenReturn(passengerResponse);

        // when
        PassengerResponse result = passengerService.createPassenger(passengerRequest);

        // then
        assertNotNull(result);
        assertEquals("sasha", result.getName());
        assertEquals("sasha@mail.com", result.getEmail());
        verify(passengerRepository, times(1)).save(passenger);
    }

    @Test
    void createPassenger_AlreadyExists() {
        // given
        when(passengerRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.of(new Passenger()));

        // when / then
        assertThrows(InvalidPassengerOperationException.class,
            () -> passengerService.createPassenger(passengerRequest));
        verify(passengerRepository, times(1)).findByEmail("sasha@mail.com");
    }

    @Test
    void deletePassenger_Success() {
        // given
        when(passengerRepository.existsById(1L)).thenReturn(true);
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        assertFalse(passenger.getIsDeleted());
        // when
        passengerService.deletePassenger(1L);

        // then
        assertTrue(passenger.getIsDeleted());
        verify(passengerRepository, times(1)).save(passenger);
    }

    @Test
    void deletePassenger_NotFound() {
        // given
        when(passengerRepository.existsById(1L)).thenReturn(false);

        // when / then
        assertThrows(PassengerNotFoundException.class, () -> passengerService.deletePassenger(1L));
        verify(passengerRepository, times(1)).existsById(1L);
    }

    @Test
    void updatePassenger_Success() {
        // given
        when(passengerRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.of(passenger));
        when(passengerMapper.toDto(passenger)).thenReturn(passengerResponse);

        // when
        PassengerResponse result = passengerService.updatePassenger("sasha@mail.com", passengerRequest);

        // then
        assertNotNull(result);
        assertEquals("sasha", result.getName());
        assertEquals("sasha@mail.com", result.getEmail());
        verify(passengerRepository, times(1)).save(passenger);
    }

    @Test
    void updatePassenger_NotFound() {
        // given
        when(passengerRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.empty());

        // when / then
        assertThrows(PassengerNotFoundException.class,
            () -> passengerService.updatePassenger("sasha@mail.com", passengerRequest));
        verify(passengerRepository, times(1)).findByEmail("sasha@mail.com");
    }

    @Test
    void updatePassenger_AlreadyExists() {
        // given
        when(passengerRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.of(passenger));
        when(passengerRepository.findByEmail("new@mail.com")).thenReturn(Optional.of(new Passenger()));

        // Устанавливаем новый email в запросе
        passengerRequest.setEmail("new@mail.com");

        // when / then
        assertThrows(InvalidPassengerOperationException.class, () ->
            passengerService.updatePassenger("sasha@mail.com", passengerRequest)
        );

        // Проверяем вызовы методов
        verify(passengerRepository, times(1)).findByEmail("sasha@mail.com");
        verify(passengerRepository, times(1)).findByEmail("new@mail.com");
    }
}