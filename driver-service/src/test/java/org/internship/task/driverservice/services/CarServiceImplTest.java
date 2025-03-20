package org.internship.task.driverservice.services;

import org.internship.task.driverservice.dto.cars.CarListResponse;
import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.cars.CarResponse;
import org.internship.task.driverservice.entities.Car;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.exceptions.carExceptions.CarNotFoundException;
import org.internship.task.driverservice.exceptions.carExceptions.InvalidCarOperationException;
import org.internship.task.driverservice.exceptions.driverException.DriverNotFoundException;
import org.internship.task.driverservice.mappers.CarMapper;
import org.internship.task.driverservice.repositories.CarRepository;
import org.internship.task.driverservice.repositories.DriverRepository;
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

import static org.internship.task.driverservice.util.TestDataForCar.*;
import static org.internship.task.driverservice.util.TestDataForDriver.createDriver;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    @Mock
    private CarRepository carRepository;
    @Mock
    private DriverRepository driverRepository;
    @Mock
    private CarMapper carMapper;
    @InjectMocks
    private CarServiceImpl carService;

    private Car car;
    private CarResponse carResponse;
    private CarRequest carRequest;
    private Driver driver;

    @BeforeEach
    void setUp() {
        car = createCar();
        carResponse = createCarResponse();
        carRequest = createCarRequest();

        driver = createDriver();
    }

    @Test
    void getAllCars_ReturnsNonEmptyList() {
        when(carRepository.findAllByOrderByIdAsc()).thenReturn(List.of(car));
        when(carMapper.toDtoList(List.of(car))).thenReturn(List.of(carResponse));

        ResponseEntity<CarListResponse> result = carService.getAllCars();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().cars().size());
        verify(carRepository).findAllByOrderByIdAsc();
    }

    @Test
    void getAllCars_ReturnsNoContent() {
        when(carRepository.findAllByOrderByIdAsc()).thenReturn(Collections.emptyList());

        ResponseEntity<CarListResponse> result = carService.getAllCars();

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(carRepository).findAllByOrderByIdAsc();
    }

    @Test
    void getAllCarsByStatus_ReturnsNonEmptyList() {
        when(carRepository.findByIsDeletedOrderByIdAsc(false)).thenReturn(List.of(car));
        when(carMapper.toDtoList(List.of(car))).thenReturn(List.of(carResponse));

        ResponseEntity<CarListResponse> result = carService.getAllCarsByStatus(false);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().cars().size());
        verify(carRepository).findByIsDeletedOrderByIdAsc(false);
    }

    @Test
    void getCarById_ReturnsCarResponse() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carMapper.toDto(car)).thenReturn(carResponse);

        CarResponse result = carService.getCarById(1L);

        assertEquals("1234AB6", result.getCarNumber());
        verify(carRepository).findById(1L);
    }

    @Test
    void getCarById_ThrowsCarNotFoundException() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> carService.getCarById(1L));
    }

    @Test
    void createCar_ReturnsCarResponse() {
        when(carRepository.findByCarNumber("1234AB6")).thenReturn(Optional.empty());
        when(driverRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.of(driver));
        when(carMapper.toEntity(carRequest)).thenReturn(car);
        when(carMapper.toDto(car)).thenReturn(carResponse);

        CarResponse result = carService.createCar(carRequest, "sasha@mail.com");

        assertEquals("1234AB6", result.getCarNumber());
        verify(carRepository).save(car);
    }

    @Test
    void createCar_ThrowsInvalidCarOperationException() {
        when(carRepository.findByCarNumber("1234AB6")).thenReturn(Optional.of(new Car()));

        assertThrows(InvalidCarOperationException.class,
                () -> carService.createCar(carRequest, "sasha@mail.com"));
    }

    @Test
    void createCar_ThrowsDriverNotFoundException() {
        when(carRepository.findByCarNumber("1234AB6")).thenReturn(Optional.empty());
        when(carMapper.toEntity(carRequest)).thenReturn(car);
        car.setIsDeleted(false);
        when(driverRepository.findByEmail("sasha@mail.com")).thenReturn(Optional.empty());
        car.setDriver(driver);

        assertThrows(DriverNotFoundException.class,
                () -> carService.createCar(carRequest, "sasha@mail.com"));
    }

    @Test
    void updateCar_ReturnsCarResponse() {
        when(carRepository.findByCarNumber("1234AB6")).thenReturn(Optional.of(car));
        when(carMapper.toDto(car)).thenReturn(carResponse);

        CarResponse result = carService.updateCar("1234AB6", carRequest);

        assertEquals("1234AB6", result.getCarNumber());
        verify(carRepository).save(car);
    }

    @Test
    void updateCar_ThrowsCarNotFoundException() {
        when(carRepository.findByCarNumber("1234AB6")).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class,
                () -> carService.updateCar("1234AB6", carRequest));
    }

    @Test
    void deleteCar_Success() {
        when(carRepository.existsById(1L)).thenReturn(true);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        car.setIsDeleted(true);

        carService.deleteCar(1L);

        assertTrue(car.getIsDeleted());
        verify(carRepository).save(car);
    }

    @Test
    void deleteCar_ThrowsCarNotFoundException() {
        assertThrows(CarNotFoundException.class, () -> carService.deleteCar(1L));

    }
}