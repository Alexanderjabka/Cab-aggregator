package org.internship.task.driverservice.util;

import static org.internship.task.driverservice.util.TestDataForCar.createCar;

import java.util.List;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
import org.internship.task.driverservice.entities.Car;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.enums.Gender;

public class TestDataForDriver {
    private static final Long ID = 1L;

    private static final String NAME = "sasha";

    private static final String EMAIL = "sasha@mail.com";

    private static final String PHONE_NUMBER = "+375294323756";

    private static final Gender GENDER = Gender.MALE;

    private static final List<Car> CARS = List.of(createCar());

    private static final List<Long> CARS_ID = List.of(createCar().getId());

    private static final Boolean IS_DELETED = false;

    private static final Boolean IS_IN_RIDE = false;

    public static Driver createDriver() {
        return new Driver(ID, NAME, EMAIL, PHONE_NUMBER, GENDER, CARS, IS_DELETED, IS_IN_RIDE);
    }

    public static DriverResponse createDriverResponse() {
        return new DriverResponse(ID, NAME, EMAIL, PHONE_NUMBER, GENDER, CARS_ID, IS_DELETED, IS_IN_RIDE);
    }

    public static DriverRequest createDriverRequest() {
        return new DriverRequest(NAME, EMAIL, PHONE_NUMBER, GENDER);
    }
}
