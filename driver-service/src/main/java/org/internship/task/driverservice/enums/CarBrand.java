package org.internship.task.driverservice.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum CarBrand {
    TOYOTA("Toyota"),
    FORD("Ford"),
    BMW("BMW"),
    MERCEDES_BENZ("Mercedes-Benz"),
    AUDI("Audi"),
    HONDA("Honda"),
    TESLA("Tesla"),
    CHEVROLET("Chevrolet"),
    NISSAN("Nissan"),
    VOLKSWAGEN("Volkswagen");
    private final String displayName;
}
