package org.internship.task.passengerservice.DTO;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class PassengerResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Boolean isDeleted;


}
