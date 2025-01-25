package org.internship.task.passengerservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "passenger")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {

    private final static String PHONE_NUMBER = "^\\+375[\\- ]?\\(?\\d{2}\\)?[\\- ]?\\d{7,9}$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min = 2, max = 30, message = "name must be between 2 and 30 characters")
    @NotBlank(message = "name cannot be empty")
    @Column(name = "name")
    private String name;

    @Email(message = "invalid email format")
    @NotBlank(message = "email cannot be empty")
    @Column(name = "email")
    private String email;


    @Pattern(regexp = PHONE_NUMBER, message = "phone number must match +375 format")
    @Size(min = 10, max = 15, message = "phone number must be between 10 and 15 characters")
    @NotBlank(message = "phone number cannot be empty")
    @Column(name = "phoneNumber")
    private String phoneNumber;


    @Column(name = "isDeleted")
    private Boolean isDeleted = false;


}
