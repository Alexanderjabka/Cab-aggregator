package org.internship.task.ratingservice.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {

    private Long id;

    private Long driverId;

    private Long passengerId;

    private Short score;

    private String comment;

    private Long rideId;

    private Boolean isDeleted;
}
