package org.internship.task.driverservice.dto.cars;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.internship.task.driverservice.enums.CarBrand;
import org.internship.task.driverservice.enums.Color;

import static org.internship.task.driverservice.util.constantMessages.validationMessages.CarValidationMessages.*;
import static org.internship.task.driverservice.util.regEx.CarRegEx.CAR_NUMBER;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarRequest {
    @NotNull(message = COLOR_CANNOT_BE_EMPTY)
    private Color color;

    @NotNull(message = CAR_BRAND_CANNOT_BE_EMPTY)
    private CarBrand carBrand;

    @NotBlank(message = CAR_NUMBER_CANNOT_BE_EMPTY)
    @Pattern(regexp = CAR_NUMBER, message = INVALID_FORMAT_OF_CAR_NUMBER)
    private String carNumber;

    @Min(value = 1990, message = CAR_YEAR_CANNOT_BE_LESS_THAN_1990)
    @Max(value = 2025, message = CAR_YEAR_CANNOT_BE_GREATER_THAN_2025)
    private short carYear;
}
