package org.internship.task.driverservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum Color {
    RED("Red"),
    BLUE("Blue"),
    GREEN("Green"),
    YELLOW("Yellow"),
    BLACK("Black"),
    WHITE("White"),
    PURPLE("Purple"),
    ORANGE("Orange"),
    PINK("Pink"),
    BROWN("Brown");
    private final String displayName;
}
