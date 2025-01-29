package org.internship.task.driverservice.mappers;

import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.cars.CarResponse;
import org.internship.task.driverservice.entities.Car;

import java.util.List;
import java.util.stream.Collectors;

public class CarMapper {
    public static Car toEntity(CarRequest carRequest) {
        Car car = new Car();
        car.setColor(carRequest.getColor());
        car.setCarBrand(carRequest.getCarBrand());
        car.setCarNumber(carRequest.getCarNumber());
        car.setCarYear(carRequest.getCarYear());
        return car;
    }

    public static CarResponse toDto(Car car) {
        CarResponse carResponse = new CarResponse();
        carResponse.setCarId(car.getId());
        carResponse.setColor(car.getColor());
        carResponse.setCarBrand(car.getCarBrand());
        carResponse.setCarNumber(car.getCarNumber());
        carResponse.setCarYear(car.getCarYear());
        carResponse.setIsDeleted(car.getIsDeleted());

        if (car.getDriver() != null) {
            carResponse.setDriverId(car.getDriver().getId());
        }

        return carResponse;
    }

    public static List<CarResponse> toDtoList(List<Car> cars) {
        return cars.stream().map(CarMapper::toDto).collect(Collectors.toList());
    }

    public static void toEntity(CarRequest carRequest, Car car) {
        car.setColor(carRequest.getColor());
        car.setCarBrand(carRequest.getCarBrand());
        car.setCarNumber(carRequest.getCarNumber());
        car.setCarYear(carRequest.getCarYear());
    }
}
