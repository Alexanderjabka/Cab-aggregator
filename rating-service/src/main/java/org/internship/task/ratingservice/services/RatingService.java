package org.internship.task.ratingservice.services;

import java.util.List;
import org.internship.task.ratingservice.dto.RatingRequest;
import org.internship.task.ratingservice.dto.RatingResponse;
import org.springframework.http.ResponseEntity;

public interface RatingService {

    ResponseEntity<List<RatingResponse>> getAllRatings();

    RatingResponse createRating(RatingRequest ratingRequest);

    ResponseEntity<String> deleteRating(Long ratingId);

    double getAverageDriverRating(Long driverId);

    double getAveragePassengerRating(Long passengerId);
}
