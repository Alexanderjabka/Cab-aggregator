package org.internship.task.ratingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.internship.task.ratingservice.enums.WhoRate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {

    private Long id;

    private Long driverId;

    private Long passengerId;

    private Long rideId;

    private Short score;

    private String comment;

    private WhoRate whoRate;

    private Boolean isDeleted;
}
