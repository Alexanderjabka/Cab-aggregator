package org.internship.task.ratingservice.services;

import java.util.List;
import org.internship.task.ratingservice.dto.RatingRequest;
import org.internship.task.ratingservice.dto.RatingResponse;
import org.springframework.http.ResponseEntity;

public interface RatingService {

    public ResponseEntity<List<RatingResponse>> getAllRatings();

    public RatingResponse createRating(RatingRequest ratingRequest);

    public ResponseEntity<String> deleteRating(Long ratingId);

    public double getAverageDriverRating(Long driverId);

    public double getAveragePassengerRating(Long passengerId);
}
