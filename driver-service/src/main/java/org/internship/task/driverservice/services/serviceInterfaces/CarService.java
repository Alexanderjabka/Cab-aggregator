package org.internship.task.driverservice.services.serviceInterfaces;

import java.util.List;

import org.internship.task.driverservice.dto.cars.CarListResponse;
import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.cars.CarResponse;
import org.springframework.http.ResponseEntity;

public interface CarService {
    ResponseEntity<CarListResponse> getAllCars();

    ResponseEntity<CarListResponse> getAllCarsByStatus(boolean status);

    CarResponse getCarById(Long id);

    CarResponse createCar(CarRequest carRequest, String driverEmail);

    CarResponse updateCar(String carNumber, CarRequest carRequest);

    void deleteCar(Long id);
}
