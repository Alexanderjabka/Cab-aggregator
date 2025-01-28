package org.internship.task.driverservice.dto.drivers;

import org.internship.task.driverservice.entities.Car;
import org.internship.task.driverservice.enums.Gender;

public record DriverResponse(
        Long driverId,
        String name,
        String email,
        String phoneNumber,
        Gender gender,
        Car car,
        boolean isDeleted

){}
