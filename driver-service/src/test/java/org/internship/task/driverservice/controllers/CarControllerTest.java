package org.internship.task.driverservice.controllers;

import static org.internship.task.driverservice.util.TestDataForCar.createCarRequest;
import static org.internship.task.driverservice.util.TestDataForCar.createCarResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.internship.task.driverservice.dto.cars.CarListResponse;
import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.cars.CarResponse;
import org.internship.task.driverservice.services.CarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarServiceImpl carService;

    @InjectMocks
    private CarController carController;

    private CarResponse carResponse;
    private CarRequest carRequest;


    @BeforeEach
    void setUp() {
        carResponse = createCarResponse();
        carRequest = createCarRequest();
    }

    @Test
    void getAllCars_ReturnsNonEmptyList() {
        // Arrange
        CarListResponse listResponse = new CarListResponse(List.of(carResponse));

        when(carService.getAllCars()).thenReturn(ResponseEntity.ok(listResponse));

        // Act
        ResponseEntity<CarListResponse> result = carController.getAllCars();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().cars().size());
    }

    @Test
    void getAllCarsByStatus_ReturnsNonEmptyList() {
        // Arrange
        CarListResponse listResponse = new CarListResponse(List.of(carResponse));

        when(carService.getAllCarsByStatus(false)).thenReturn(ResponseEntity.ok(listResponse));

        // Act
        ResponseEntity<CarListResponse> result = carController.getAllCarsByStatus(false);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().cars().size());
    }

    @Test
    void getCarById_ReturnsCarResponse() {
        // Arrange
        when(carService.getCarById(1L)).thenReturn(carResponse);

        // Act
        ResponseEntity<CarResponse> result = carController.getCarById(1L);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("1234AB6", result.getBody().getCarNumber());
    }

    @Test
    void createCar_ReturnsCreated() {
        // Arrange
        when(carService.createCar(carRequest, "sasha@mail.com")).thenReturn(carResponse);

        // Act
        ResponseEntity<CarResponse> result = carController.createCar(carRequest, "sasha@mail.com");

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("1234AB6", result.getBody().getCarNumber());
    }

    @Test
    void updateCar_ReturnsUpdatedCar() {
        // Arrange
        when(carService.updateCar("1234AB6", carRequest)).thenReturn(carResponse);

        // Act
        ResponseEntity<CarResponse> result = carController.updateCar("1234AB6", carRequest);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("1234AB6", result.getBody().getCarNumber());
    }

    @Test
    void deleteCar_ReturnsNoContent() {
        // Arrange
        doNothing().when(carService).deleteCar(1L);

        // Act
        ResponseEntity<Void> result = carController.deleteCar(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(carService).deleteCar(1L);
    }
}