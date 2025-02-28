package org.internship.task.driverservice.controllers;

import static org.internship.task.driverservice.util.TestDataForDriver.createDriverRequest;
import static org.internship.task.driverservice.util.TestDataForDriver.createDriverResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.internship.task.driverservice.dto.drivers.DriverListResponse;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
import org.internship.task.driverservice.services.DriverServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DriverControllerTest {

    @Mock
    private DriverServiceImpl driverService;

    @InjectMocks
    private DriverController driverController;

    private DriverResponse driverResponse;
    private DriverRequest driverRequest;

    @BeforeEach
    void setUp() {
        driverResponse = createDriverResponse();
        driverRequest = createDriverRequest();
    }

    @Test
    void getAllDrivers_ReturnsNonEmptyList() {
        // Arrange
        DriverListResponse listResponse = new DriverListResponse(List.of(driverResponse));
        when(driverService.getAllDrivers()).thenReturn(ResponseEntity.ok(listResponse));

        // Act
        ResponseEntity<DriverListResponse> result = driverController.getAllDrivers();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().drivers().size());
    }

    @Test
    void getAllDriversByStatus_ReturnsNonEmptyList() {
        // Arrange
        DriverListResponse listResponse = new DriverListResponse(List.of(driverResponse));
        when(driverService.getAllDriversByStatus(false)).thenReturn(ResponseEntity.ok(listResponse));

        // Act
        ResponseEntity<DriverListResponse> result = driverController.getAllDriversByStatus(false);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().drivers().size());
    }

    @Test
    void getDriverById_ReturnsDriverResponse() {
        // Arrange
        when(driverService.getDriverById(1L)).thenReturn(driverResponse);

        // Act
        ResponseEntity<DriverResponse> result = driverController.getDriverById(1L);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("sasha@mail.com", result.getBody().getEmail());
    }

    @Test
    void createDriver_ReturnsCreated() {
        // Arrange
        when(driverService.createDriver(driverRequest)).thenReturn(driverResponse);

        // Act
        ResponseEntity<DriverResponse> result = driverController.createDriver(driverRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("sasha@mail.com", result.getBody().getEmail());
    }

    @Test
    void updateDriver_ReturnsUpdatedDriver() {
        // Arrange
        when(driverService.updateDriver("sasha@mail.com", driverRequest)).thenReturn(driverResponse);

        // Act
        ResponseEntity<DriverResponse> result = driverController.updateDriver("sasha@mail.com", driverRequest);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("sasha@mail.com", result.getBody().getEmail());
    }

    @Test
    void assignDriver_ReturnsDriverInRide() {
        // Arrange
        driverResponse.setIsInRide(true);

        when(driverService.getFirstFreeDriverAndChangeStatus()).thenReturn(driverResponse);

        // Act
        ResponseEntity<DriverResponse> result = driverController.assignDriver();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().getIsInRide());
    }

    @Test
    void releaseDriver_ReturnsNoContent() {
        // Arrange
        doNothing().when(driverService).releaseDriver(1L);

        // Act
        ResponseEntity<Void> result = driverController.releaseDriver(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(driverService).releaseDriver(1L);
    }

    @Test
    void deleteDriver_ReturnsNoContent() {
        // Arrange
        doNothing().when(driverService).deleteDriver(1L);

        // Act
        ResponseEntity<Void> result = driverController.deleteDriver(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(driverService).deleteDriver(1L);
    }
}