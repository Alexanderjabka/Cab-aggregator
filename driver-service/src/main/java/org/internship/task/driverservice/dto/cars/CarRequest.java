package org.internship.task.driverservice.dto.cars;

import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.enums.CarBrand;
import org.internship.task.driverservice.enums.Color;

public record CarRequest (
        Color color,
        CarBrand carBrand,
        String carNumber,
        short carYear,
        Driver driver

){}
