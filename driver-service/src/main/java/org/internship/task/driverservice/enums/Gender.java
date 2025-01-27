package org.internship.task.driverservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum Gender {
    MALE("male"),
    FEMALE("female");
    private final String displayGender;
}
