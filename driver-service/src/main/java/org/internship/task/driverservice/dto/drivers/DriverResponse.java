package org.internship.task.driverservice.dto.drivers;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.internship.task.driverservice.enums.Gender;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Gender gender;
    private List<Long> carId;
    private Boolean isDeleted;
}
