package org.internship.task.driverservice.testDataIT;

import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.entities.Car;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.enums.CarBrand;
import org.internship.task.driverservice.enums.Color;
import org.internship.task.driverservice.enums.Gender;

import java.util.List;

public class DataForIT {
    public static final long INVALID_ID = 999L;
    public static final long ID = 1L;
    public static final Color COLOR = Color.RED;
    public static final CarBrand CAR_BRAND = CarBrand.BMW;
    public static final String CAR_NUMBER = "1234AB6";
    public static final String UPDATE_CAR_NUMBER = "4321AB6";
    public static final String INVALID_CAR_NUMBER = "1234";
    public static final short CAR_YEAR = 2004;
    public static final boolean IS_DELETED = false;

    public static final String NAME = "sasha";
    public static final String EMAIL = "sasha@example.com";
    public static final String UPDATE_EMAIL = "updated_sasha@example.com";
    public static final String INVALID_EMAIL = "invalid_sashaexample.com";
    public static final String PHONE_NUMBER = "+375298876654";
    public static final Gender GENDER = Gender.MALE;
    public static final boolean IS_IN_RIDE = false;

    public static final Driver CREATE_DRIVER = new Driver(
            ID,
            NAME,
            EMAIL,
            PHONE_NUMBER,
            GENDER,
            null,
            IS_DELETED,
            IS_IN_RIDE
    );

    public static final Car CREATE_CAR = new Car(
            ID,
            COLOR,
            CAR_BRAND,
            CAR_NUMBER,
            CAR_YEAR,
            null,
            IS_DELETED
    );
    public static final CarRequest CREATE_CAR_REQUEST = new CarRequest(
            COLOR,
            CAR_BRAND,
            CAR_NUMBER,
            CAR_YEAR
    );
    public static final CarRequest CREATE_UPDATE_CAR_REQUEST = new CarRequest(
            COLOR,
            CAR_BRAND,
            UPDATE_CAR_NUMBER,
            CAR_YEAR
    );
    public static final CarRequest CREATE_INVALID_CAR_REQUEST = new CarRequest(
            COLOR,
            CAR_BRAND,
            INVALID_CAR_NUMBER,
            CAR_YEAR
    );
    public static final DriverRequest CREATE_DRIVER_REQUEST = new DriverRequest(
            NAME,
            EMAIL,
            PHONE_NUMBER,
            GENDER
    );
    public static final DriverRequest CREATE_INVALID_DRIVER_REQUEST = new DriverRequest(
            NAME,
            INVALID_EMAIL,
            PHONE_NUMBER,
            GENDER
    );
    public static final DriverRequest CREATE_UPDATE_DRIVER_REQUEST = new DriverRequest(
            NAME,
            UPDATE_EMAIL,
            PHONE_NUMBER,
            GENDER
    );

    static {
        CREATE_DRIVER.setCars(List.of(CREATE_CAR));
        CREATE_CAR.setDriver(CREATE_DRIVER);
    }
}