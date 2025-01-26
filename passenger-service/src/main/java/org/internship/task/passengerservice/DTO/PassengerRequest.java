package org.internship.task.passengerservice.DTO;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PassengerRequest {

    private String name;
    private String email;
    private String phoneNumber;
    private Boolean isDeleted;

}
