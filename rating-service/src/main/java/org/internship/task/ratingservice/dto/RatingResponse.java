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
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driverId")
    private Long driverId;

    @Column(name = "passengerId")
    private Long passengerId;

    @Column(name = "score")
    private Short score;

    @Column(name = "comment")
    private String comment;

    @Column(name = "rideId")
    private Long rideId;
}
