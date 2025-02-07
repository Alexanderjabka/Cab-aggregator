package org.internship.task.ratingservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum WhoRate {
    DRIVER(0),
    PASSENGER(1);
    private final int description;
}
