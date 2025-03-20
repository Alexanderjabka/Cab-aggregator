package org.internship.task.ratingservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.internship.task.ratingservice.enums.WhoRate;

@Entity
@Table(name = "rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rideId")
    private Long rideId;

    @Column(name = "driverId")
    private Long driverId;

    @Column(name = "passengerId")
    private Long passengerId;

    @Column(name = "score")
    private Short score;

    @Column(name = "comment")
    private String comment;

    @Column(name = "whoRate")
    private WhoRate whoRate;

    @Column(name = "isDeleted")
    private Boolean isDeleted = false;
}
