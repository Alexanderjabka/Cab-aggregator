package org.internship.task.driverservice.mappers;

import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
import org.internship.task.driverservice.entities.Car;
import org.internship.task.driverservice.entities.Driver;

import java.util.List;
import java.util.stream.Collectors;

public class DriverMapper {
    // Преобразование DriverRequest в Driver
    public static Driver toEntity(DriverRequest driverRequest) {
        Driver driver = new Driver();
        driver.setName(driverRequest.getName());
        driver.setEmail(driverRequest.getEmail());
        driver.setPhoneNumber(driverRequest.getPhoneNumber());
        driver.setGender(driverRequest.getGender());
        return driver;
    }

    // Преобразование Driver в DriverResponse
    public static DriverResponse toDto(Driver driver) {
        DriverResponse driverResponse = new DriverResponse();
        driverResponse.setDriverId(driver.getId());
        driverResponse.setName(driver.getName());
        driverResponse.setEmail(driver.getEmail());
        driverResponse.setPhoneNumber(driver.getPhoneNumber());
        driverResponse.setGender(driver.getGender());
        driverResponse.setIsDeleted(driver.getIsDeleted());

        if (driver.getCars() != null && !driver.getCars().isEmpty()) {
            List<Long> carIds = driver.getCars().stream()
                    .filter(car -> !car.getIsDeleted()) // Фильтруем только машины, которые не удалены
                    .map(Car::getId)
                    .collect(Collectors.toList());
            driverResponse.setCarId(carIds);
        }

            return driverResponse;
    }

    // Преобразование списка Driver в список DriverResponse
    public static List<DriverResponse> toDtoList(List<Driver> drivers) {
        return drivers.stream().map(DriverMapper::toDto).collect(Collectors.toList());
    }

    // Маппинг DriverRequest в существующий Driver
    public static void toEntity(DriverRequest driverRequest, Driver driver) {
        driver.setName(driverRequest.getName());
        driver.setEmail(driverRequest.getEmail());
        driver.setPhoneNumber(driverRequest.getPhoneNumber());
        driver.setGender(driverRequest.getGender());
    }
}
