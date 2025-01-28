package org.internship.task.passengerservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static org.internship.task.passengerservice.util.constantMessages.ValidationMessages.PHONE_NUMBER_BLANK;
import static org.internship.task.passengerservice.util.constantMessages.ValidationMessages.NAME_BLANK;
import static org.internship.task.passengerservice.util.constantMessages.ValidationMessages.NAME_SIZE;
import static org.internship.task.passengerservice.util.constantMessages.ValidationMessages.EMAIL_BLANK;
import static org.internship.task.passengerservice.util.constantMessages.ValidationMessages.EMAIL_INVALID;
import static org.internship.task.passengerservice.util.constantMessages.ValidationMessages.PHONE_NUMBER_INVALID;
import static org.internship.task.passengerservice.util.constantMessages.ValidationMessages.PHONE_NUMBER_SIZE;
import static org.internship.task.passengerservice.util.reqEx.PassengerReqEx.PHONE_NUMBER;

@Entity
@Table(name = "passenger")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min = 2, max = 30, message = NAME_SIZE)
    @NotBlank(message = NAME_BLANK)
    @Column(name = "name")
    private String name;

    @Email(message = EMAIL_INVALID)
    @NotBlank(message = EMAIL_BLANK)
    @Column(name = "email")
    private String email;

    @Pattern(regexp = PHONE_NUMBER, message = PHONE_NUMBER_INVALID)
    @Size(min = 10, max = 15, message = PHONE_NUMBER_SIZE)
    @NotBlank(message = PHONE_NUMBER_BLANK)
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
