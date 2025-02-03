package org.internship.task.driverservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum Color {
    RED(0),
    BLUE(1),
    GREEN(2),
    YELLOW(3),
    BLACK(4),
    WHITE(5),
    PURPLE(6),
    ORANGE(7),
    PINK(8),
    BROWN(9);
    private final int displayName;
}
