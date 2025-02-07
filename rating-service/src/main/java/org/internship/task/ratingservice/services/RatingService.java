package org.internship.task.ratingservice.services;

import org.internship.task.ratingservice.dto.RatingRequest;
import org.internship.task.ratingservice.dto.RatingResponse;

public interface RatingService {
    public RatingResponse createRating(RatingRequest ratingRequest);

    public double getAverageDriverRating(Long driverId);

    public double getAveragePassengerRating(Long passengerId);
}
