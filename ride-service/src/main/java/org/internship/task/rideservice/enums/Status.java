package org.internship.task.rideservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum Status {
    CREATED("created"),
    ACCEPTED("accepted"),
    EN_ROUTE_TO_PASSENGER("in route to passenger"),
    EN_ROUTE_TO_DESTINATION("in route to destination"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private final String description;
}
