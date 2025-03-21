package org.internship.task.driverservice.util;

import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.cars.CarResponse;
import org.internship.task.driverservice.entities.Car;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.enums.CarBrand;
import org.internship.task.driverservice.enums.Color;

import static org.internship.task.driverservice.util.TestDataForDriver.createDriver;

public class TestDataForCar {

    private static final Long ID = 1L;

    private static final Color COLOR = Color.RED;

    private static final CarBrand CAR_BRAND = CarBrand.BMW;

    private static final String CAR_NUMBER = "1234AB6";

    private static final short CAR_YEAR = 2004;

    private static final Driver DRIVER = createDriver();

    private static final Boolean IS_DELETED = false;

    private static final Long DRIVER_ID = createDriver().getId();

    public static Car createCar() {
        return new Car(ID, COLOR, CAR_BRAND, CAR_NUMBER, CAR_YEAR, DRIVER, IS_DELETED);
    }

    public static CarResponse createCarResponse() {
        return new CarResponse(ID, COLOR, CAR_BRAND, CAR_NUMBER, CAR_YEAR, DRIVER_ID, IS_DELETED);
    }

    public static CarRequest createCarRequest() {
        return new CarRequest(COLOR, CAR_BRAND, CAR_NUMBER, CAR_YEAR);
    }
}
