package org.internship.task.rideservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum Status {
    CREATED(0),
    ACCEPTED(1),
    EN_ROUTE_TO_PASSENGER(2),
    EN_ROUTE_TO_DESTINATION(3),
    COMPLETED(4),
    CANCELLED(5);

    private final int description;
}
