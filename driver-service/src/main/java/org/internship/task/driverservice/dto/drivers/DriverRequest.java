package org.internship.task.driverservice.dto.drivers;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.internship.task.driverservice.enums.Gender;

import static org.internship.task.driverservice.util.constantMessages.validationMessages.DriverValidationMessages.NAME_NOT_EMPTY;
import static org.internship.task.driverservice.util.constantMessages.validationMessages.DriverValidationMessages.NAME_SIZE;
import static org.internship.task.driverservice.util.constantMessages.validationMessages.DriverValidationMessages.EMAIL_INVALID;
import static org.internship.task.driverservice.util.constantMessages.validationMessages.DriverValidationMessages.EMAIL_NOT_EMPTY;
import static org.internship.task.driverservice.util.constantMessages.validationMessages.DriverValidationMessages.PHONE_NUMBER_INVALID;
import static org.internship.task.driverservice.util.constantMessages.validationMessages.DriverValidationMessages.PHONE_NUMBER_SIZE;
import static org.internship.task.driverservice.util.constantMessages.validationMessages.DriverValidationMessages.PHONE_NUMBER_NOT_EMPTY;
import static org.internship.task.driverservice.util.constantMessages.validationMessages.DriverValidationMessages.GENDER_NOT_EMPTY;

import static org.internship.task.driverservice.util.regEx.DriverRegEx.PHONE_NUMBER;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverRequest{
        @NotBlank(message = NAME_NOT_EMPTY)
        @Size(min = 2, max = 30, message = NAME_SIZE)
        private String name;

        @Email(message = EMAIL_INVALID)
        @NotBlank(message = EMAIL_NOT_EMPTY)
        private String email;

        @Pattern(regexp = PHONE_NUMBER, message = PHONE_NUMBER_INVALID)
        @Size(min = 10, max = 15, message = PHONE_NUMBER_SIZE)
        @NotBlank(message = PHONE_NUMBER_NOT_EMPTY)
        private String phoneNumber;

        @NotNull(message = GENDER_NOT_EMPTY)
        private Gender gender;
}
