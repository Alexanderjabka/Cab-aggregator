package org.internship.task.driverservice.services.serviceInterfaces;

import java.util.List;
import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.cars.CarResponse;

public interface CarService {
    List<CarResponse> getAllCars();

    List<CarResponse> getAllCarsByStatus(boolean status);

    CarResponse getCarById(Long id);

    CarResponse createCar(CarRequest carRequest, String driverEmail);

    CarResponse updateCar(String carNumber, CarRequest carRequest);

    void deleteCar(Long id);
}
