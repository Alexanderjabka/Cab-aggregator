package org.internship.task.passengerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PassengerResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Boolean isDeleted;
}
