package org.internship.task.driverservice.controllers;

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

import java.util.List;

import static org.internship.task.driverservice.util.TestDataForCar.createCarRequest;
import static org.internship.task.driverservice.util.TestDataForCar.createCarResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        CarListResponse listResponse = new CarListResponse(List.of(carResponse));

        when(carService.getAllCars()).thenReturn(ResponseEntity.ok(listResponse));

        ResponseEntity<CarListResponse> result = carController.getAllCars();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().cars().size());
        verify(carService).getAllCars();
    }

    @Test
    void getAllCarsByStatus_ReturnsNonEmptyList() {
        CarListResponse listResponse = new CarListResponse(List.of(carResponse));

        when(carService.getAllCarsByStatus(false)).thenReturn(ResponseEntity.ok(listResponse));

        ResponseEntity<CarListResponse> result = carController.getAllCarsByStatus(false);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().cars().size());
        verify(carService).getAllCarsByStatus(false);
    }

    @Test
    void getCarById_ReturnsCarResponse() {
        when(carService.getCarById(1L)).thenReturn(carResponse);

        ResponseEntity<CarResponse> result = carController.getCarById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("1234AB6", result.getBody().getCarNumber());
        verify(carService).getCarById(1L);
    }

    @Test
    void createCar_ReturnsCreated() {
        when(carService.createCar(carRequest, "sasha@mail.com")).thenReturn(carResponse);

        ResponseEntity<CarResponse> result = carController.createCar(carRequest, "sasha@mail.com");

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("1234AB6", result.getBody().getCarNumber());
        verify(carService).createCar(carRequest, "sasha@mail.com");
    }

    @Test
    void updateCar_ReturnsUpdatedCar() {
        when(carService.updateCar("1234AB6", carRequest)).thenReturn(carResponse);

        ResponseEntity<CarResponse> result = carController.updateCar("1234AB6", carRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("1234AB6", result.getBody().getCarNumber());
        verify(carService).updateCar("1234AB6", carRequest);
    }

    @Test
    void deleteCar_ReturnsNoContent() {
        doNothing().when(carService).deleteCar(1L);

        ResponseEntity<Void> result = carController.deleteCar(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(carService).deleteCar(1L);
    }
}