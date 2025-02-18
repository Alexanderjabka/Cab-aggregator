package org.internship.task.passengerservice.dto;

import static org.internship.task.passengerservice.util.constantMessages.ValidationMessages.EMAIL_BLANK;
import static org.internship.task.passengerservice.util.constantMessages.ValidationMessages.EMAIL_INVALID;
import static org.internship.task.passengerservice.util.constantMessages.ValidationMessages.NAME_BLANK;
import static org.internship.task.passengerservice.util.constantMessages.ValidationMessages.NAME_SIZE;
import static org.internship.task.passengerservice.util.constantMessages.ValidationMessages.PHONE_NUMBER_BLANK;
import static org.internship.task.passengerservice.util.constantMessages.ValidationMessages.PHONE_NUMBER_INVALID;
import static org.internship.task.passengerservice.util.constantMessages.ValidationMessages.PHONE_NUMBER_SIZE;
import static org.internship.task.passengerservice.util.reqEx.PassengerReqEx.PHONE_NUMBER;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PassengerRequest {
    @Size(min = 2, max = 30, message = NAME_SIZE)
    @NotBlank(message = NAME_BLANK)
    private String name;

    @Email(message = EMAIL_INVALID)
    @NotBlank(message = EMAIL_BLANK)
    private String email;

    @Pattern(regexp = PHONE_NUMBER, message = PHONE_NUMBER_INVALID)
    @Size(min = 10, max = 15, message = PHONE_NUMBER_SIZE)
    @NotBlank(message = PHONE_NUMBER_BLANK)
    private String phoneNumber;
}
