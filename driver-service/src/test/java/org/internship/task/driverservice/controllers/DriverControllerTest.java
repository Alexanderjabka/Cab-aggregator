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
        DriverListResponse listResponse = new DriverListResponse(List.of(driverResponse));
        when(driverService.getAllDrivers()).thenReturn(ResponseEntity.ok(listResponse));

        ResponseEntity<DriverListResponse> result = driverController.getAllDrivers();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().drivers().size());
        verify(driverService).getAllDrivers();
    }

    @Test
    void getAllDriversByStatus_ReturnsNonEmptyList() {
        DriverListResponse listResponse = new DriverListResponse(List.of(driverResponse));
        when(driverService.getAllDriversByStatus(false)).thenReturn(ResponseEntity.ok(listResponse));

        ResponseEntity<DriverListResponse> result = driverController.getAllDriversByStatus(false);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().drivers().size());
        verify(driverService).getAllDriversByStatus(false);
    }

    @Test
    void getDriverById_ReturnsDriverResponse() {
        when(driverService.getDriverById(1L)).thenReturn(driverResponse);

        ResponseEntity<DriverResponse> result = driverController.getDriverById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("sasha@mail.com", result.getBody().getEmail());
        verify(driverService).getDriverById(1L);
    }

    @Test
    void createDriver_ReturnsCreated() {
        when(driverService.createDriver(driverRequest)).thenReturn(driverResponse);

        ResponseEntity<DriverResponse> result = driverController.createDriver(driverRequest);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("sasha@mail.com", result.getBody().getEmail());
        verify(driverService).createDriver(driverRequest);
    }

    @Test
    void updateDriver_ReturnsUpdatedDriver() {
        when(driverService.updateDriver("sasha@mail.com", driverRequest)).thenReturn(driverResponse);

        ResponseEntity<DriverResponse> result = driverController.updateDriver("sasha@mail.com", driverRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("sasha@mail.com", result.getBody().getEmail());
        verify(driverService).updateDriver("sasha@mail.com", driverRequest);
    }

    @Test
    void assignDriver_ReturnsDriverInRide() {
        driverResponse.setIsInRide(true);

        when(driverService.getFirstFreeDriverAndChangeStatus()).thenReturn(driverResponse);

        ResponseEntity<DriverResponse> result = driverController.assignDriver();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().getIsInRide());
        verify(driverService).getFirstFreeDriverAndChangeStatus();
    }

    @Test
    void releaseDriver_ReturnsNoContent() {
        doNothing().when(driverService).releaseDriver(1L);

        ResponseEntity<Void> result = driverController.releaseDriver(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(driverService).releaseDriver(1L);
    }

    @Test
    void deleteDriver_ReturnsNoContent() {
        doNothing().when(driverService).deleteDriver(1L);

        ResponseEntity<Void> result = driverController.deleteDriver(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(driverService).deleteDriver(1L);
    }
}