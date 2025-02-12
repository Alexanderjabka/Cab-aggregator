package org.internship.task.driverservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum Gender {
    MALE(0),
    FEMALE(1);
    private final int displayGender;
}
