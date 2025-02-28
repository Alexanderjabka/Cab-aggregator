package org.internship.task.driverservice.util;

import static org.internship.task.driverservice.util.TestDataForDriver.createDriver;

import lombok.Getter;
import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.cars.CarResponse;
import org.internship.task.driverservice.entities.Car;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.enums.CarBrand;
import org.internship.task.driverservice.enums.Color;

public class TestDataForCar {

    @Getter
    private static final Long id = 1L;

    @Getter
    private static final Color color = Color.RED;

    @Getter
    private static final CarBrand carBrand = CarBrand.BMW;

    @Getter
    private static final String carNumber = "1234AB6";

    @Getter
    private static final short carYear = 2004;

    @Getter
    private static final Driver driver = createDriver();

    @Getter
    private static final Boolean isDeleted = false;

    @Getter
    private static final Long driverId = createDriver().getId();

    public static Car createCar() {
        return new Car(id,color,carBrand,carNumber,carYear,driver,isDeleted);
    }

    public static CarResponse createCarResponse() {
        return new CarResponse(id,color,carBrand,carNumber,carYear,driverId,isDeleted);
    }

    public static CarRequest createCarRequest() {
        return new CarRequest(color,carBrand,carNumber,carYear);
    }
}
