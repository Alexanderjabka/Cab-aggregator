package org.internship.task.passengerservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerResponse {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
}
