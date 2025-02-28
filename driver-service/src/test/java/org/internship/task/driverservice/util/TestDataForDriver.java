package org.internship.task.driverservice.util;

import static org.internship.task.driverservice.util.TestDataForCar.createCar;

import java.util.List;
import lombok.Getter;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
import org.internship.task.driverservice.entities.Car;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.enums.Gender;

public class TestDataForDriver {
    @Getter
    private static final Long id = 1L;

    @Getter
    private static final String name = "sasha";

    @Getter
    private static final String email = "sasha@mail.com";

    @Getter
    private static final String phoneNumber = "+375294323756";

    @Getter
    private static final Gender gender = Gender.MALE;

    @Getter
    private static final List<Car> cars = List.of(createCar());

    @Getter
    private static final List<Long> carsId = List.of(createCar().getId());

    @Getter
    private static final Boolean isDeleted = false;

    @Getter
    private static final Boolean isInRide = false;

    public static Driver createDriver() {
        return new Driver(id, name, email, phoneNumber, gender, cars, isDeleted, isInRide);
    }

    public static DriverResponse createDriverResponse() {
        return new DriverResponse(id, name, email, phoneNumber, gender, carsId, isDeleted, isInRide);
    }

    public static DriverRequest createDriverRequest() {
        return new DriverRequest(name, email, phoneNumber, gender);
    }
}
