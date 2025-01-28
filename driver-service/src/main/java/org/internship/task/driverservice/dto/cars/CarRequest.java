package org.internship.task.driverservice.dto.cars;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.enums.CarBrand;
import org.internship.task.driverservice.enums.Color;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarRequest {
    private Color color;
    private CarBrand carBrand;
    private String carNumber;
    private short carYear;
    private Driver driver;

}
