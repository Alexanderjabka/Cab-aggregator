package org.internship.task.driverservice.services;

import lombok.RequiredArgsConstructor;
import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    public List<DriverResponse> getAllCars() {
    }

    public List<DriverResponse> getAllCarsByStatus(boolean status) {
    }

    public DriverResponse getCarById(Long id) {
    }

    public DriverResponse createCar(CarRequest carRequest, String driverEmail) {
    }

    public void updateCar(String carNumber, CarRequest carRequest) {
    }

    public void deleteCar(Long id) {
    }
}
