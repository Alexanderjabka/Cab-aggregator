package org.internship.task.driverservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.internship.task.driverservice.enums.Gender;
@Entity
@Table(name = "driver")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Driver {
    private final static String PHONE_NUMBER = "^\\+375[\\- ]?\\(?\\d{2}\\)?[\\- ]?\\d{7,9}$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Long driverId;

    @NotBlank(message = "name cannot be empty")
    @Size(min = 2, max = 30, message = "name must be between 2 and 30 characters")
    @Column(name = "name")
    private String driverName;

    @Email(message = "invalid email format")
    @NotBlank(message = "email cannot be empty")
    @Column(name = "email")
    private String email;

    @Pattern(regexp = PHONE_NUMBER, message = "phone number must match +375 format")
    @Size(min = 10, max = 15, message = "phone number must be between 10 and 15 characters")
    @NotBlank(message = "phone number cannot be empty")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Size(min = 4,max = 6)
    @NotBlank(message = "gender cannot be empty")
    @Column(name = "gender")
    private Gender gender;

    @Size(min = 2, max = 30)
    @NotBlank(message = "car cannot be empty")
    @OneToOne(mappedBy = "driver",cascade = CascadeType.ALL)
    @Column(name = "car")
    private Car car;
}
