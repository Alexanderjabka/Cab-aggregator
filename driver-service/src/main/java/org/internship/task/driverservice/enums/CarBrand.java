package org.internship.task.driverservice.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum CarBrand {
    TOYOTA(0),
    FORD(1),
    BMW(2),
    MERCEDES_BENZ(3),
    AUDI(4),
    HONDA(5),
    TESLA(6),
    CHEVROLET(7),
    NISSAN(8),
    VOLKSWAGEN(9);
    private final int displayName;
}
