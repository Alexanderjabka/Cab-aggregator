package org.internship.task.driverservice.services;

import static org.internship.task.driverservice.util.TestDataForDriver.createDriver;
import static org.internship.task.driverservice.util.TestDataForDriver.createDriverRequest;
import static org.internship.task.driverservice.util.TestDataForDriver.createDriverResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.internship.task.driverservice.dto.drivers.DriverListResponse;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.exceptions.driverException.DriverNotFoundException;
import org.internship.task.driverservice.exceptions.driverException.HandleDriverHasNoCarException;
import org.internship.task.driverservice.exceptions.driverException.InvalidDriverOperationException;
import org.internship.task.driverservice.mappers.DriverMapper;
import org.internship.task.driverservice.repositories.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {
    @Mock
    private DriverRepository driverRepository;
    @Mock
    private DriverMapper driverMapper;
    @InjectMocks
    private DriverServiceImpl driverService;

    private Driver driver;
    private DriverResponse driverResponse;
    private DriverRequest driverRequest;

    @BeforeEach
    void setUp() {
        driver = createDriver();
        driverResponse = createDriverResponse();
        driverRequest = createDriverRequest();
    }

    @Test
    void getAllDrivers_ReturnsNonEmptyList() {
        // Arrange
        when(driverRepository.findAllByOrderByIdAsc()).thenReturn(List.of(driver));
        when(driverMapper.toDtoList(List.of(driver))).thenReturn(List.of(driverResponse));

        // Act
        ResponseEntity<DriverListResponse> result = driverService.getAllDrivers();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().drivers().size());
    }

    @Test
    void getAllDrivers_ReturnsNoContent() {
        // Arrange
        when(driverRepository.findAllByOrderByIdAsc()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<DriverListResponse> result = driverService.getAllDrivers();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void getAllDriversByStatus_ReturnsNonEmptyList() {
        // Arrange
        when(driverRepository.findByIsDeletedOrderByIdAsc(false)).thenReturn(List.of(driver));
        when(driverMapper.toDtoList(List.of(driver))).thenReturn(List.of(driverResponse));

        // Act
        ResponseEntity<DriverListResponse> result = driverService.getAllDriversByStatus(false);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().drivers().size());
    }

    @Test
    void getDriverById_ReturnsDriverResponse() {
        // Arrange
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverMapper.toDto(driver)).thenReturn(driverResponse);

        // Act
        DriverResponse result = driverService.getDriverById(1L);

        // Assert
        assertEquals("sasha@mail.com", result.getEmail());
    }

    @Test
    void getDriverById_ThrowsDriverNotFoundException() {
        // Arrange
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DriverNotFoundException.class, () -> driverService.getDriverById(1L));
    }

    @Test
    void createDriver_ReturnsDriverResponse() {
        // Arrange
        when(driverRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.empty());
        when(driverMapper.toEntity(driverRequest)).thenReturn(driver);
        when(driverMapper.toDto(driver)).thenReturn(driverResponse);

        // Act
        DriverResponse result = driverService.createDriver(driverRequest);

        // Assert
        assertEquals("sasha@mail.com", result.getEmail());
        verify(driverRepository).save(driver);
    }

    @Test
    void createDriver_ThrowsInvalidDriverOperationException() {
        // Arrange
        when(driverRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.of(new Driver()));

        // Act & Assert
        assertThrows(InvalidDriverOperationException.class, () -> driverService.createDriver(driverRequest));
    }

    @Test
    void updateDriver_ReturnsDriverResponse() {
        // Arrange
        when(driverRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.of(driver));
        when(driverMapper.toDto(driver)).thenReturn(driverResponse);

        // Act
        DriverResponse result = driverService.updateDriver("sasha@mail.com", driverRequest);

        // Assert
        assertEquals("sasha@mail.com", result.getEmail());
    }

    @Test
    void updateDriver_ThrowsDriverNotFoundException() {
        // Arrange
        when(driverRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DriverNotFoundException.class, () -> driverService.updateDriver("sasha@mail.com", driverRequest));
    }

    @Test
    void deleteDriver_Success() {
        // Arrange
        when(driverRepository.existsById(1L)).thenReturn(true);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        // Act
        driverService.deleteDriver(1L);

        // Assert
        assertTrue(driver.getIsDeleted());
        verify(driverRepository).save(driver);
    }

    @Test
    void deleteDriver_ThrowsDriverNotFoundException() {
        // Arrange
        lenient().when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DriverNotFoundException.class, () -> driverService.deleteDriver(1L));
    }

    @Test
    void getFirstFreeDriverAndChangeStatus_ReturnsDriverResponse() {
        when(driverRepository.findFirstByIsInRideFalseAndIsDeletedFalseAndCarsIsDeletedFalseOrderByIdAsc())
            .thenReturn(Optional.of(driver));

        driver.setIsInRide(true);
        when(driverRepository.save(driver)).thenReturn(driver);

        when(driverMapper.toDto(any(Driver.class))).thenAnswer(invocation -> {
            Driver mappedDriver = invocation.getArgument(0);
            driverResponse.setIsInRide(mappedDriver.getIsInRide());
            return driverResponse;
        });
        // Act
        DriverResponse result = driverService.getFirstFreeDriverAndChangeStatus();

        // Assert
        assertNotNull(result);
        assertTrue(result.getIsInRide());
    }

    @Test
    void getFirstFreeDriverAndChangeStatus_ThrowsDriverNotFoundException() {
        // Arrange
        when(driverRepository.findFirstByIsInRideFalseAndIsDeletedFalseAndCarsIsDeletedFalseOrderByIdAsc()).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(DriverNotFoundException.class, () -> driverService.getFirstFreeDriverAndChangeStatus());
    }

    @Test
    void releaseDriver_Success() {
        // Arrange
        driver.setIsInRide(true);

        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        // Act
        driverService.releaseDriver(1L);

        // Assert
        assertFalse(driver.getIsInRide());
        verify(driverRepository).save(driver);
    }

    @Test
    void releaseDriver_ThrowsHandleDriverHasNoCarException() {
        // Arrange
        driver.setIsInRide(false);

        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        // Act & Assert
        assertThrows(HandleDriverHasNoCarException.class, () -> driverService.releaseDriver(1L));
    }
}