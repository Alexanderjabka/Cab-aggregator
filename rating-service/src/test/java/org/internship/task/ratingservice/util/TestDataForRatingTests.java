package org.internship.task.ratingservice.util;

import org.internship.task.ratingservice.dto.RatingRequest;
import org.internship.task.ratingservice.dto.RatingResponse;
import org.internship.task.ratingservice.dto.clientsDto.GetRideResponse;
import org.internship.task.ratingservice.entities.Rating;
import org.internship.task.ratingservice.enums.WhoRate;

public class TestDataForRatingTests {
    private static final Long id = 1L;

    private static final Long rideId = 1L;

    private static final Long driverId = 1L;

    private static final Long passengerId = 1L;

    private static final Short score = 5;

    private static final String comment = "Good";

    private static final WhoRate whoRate = WhoRate.DRIVER;

    private static final Boolean isDeleted = false;

    public static Rating createRating() {
        return new Rating(id, rideId, driverId, passengerId, score, comment, whoRate, isDeleted);
    }

    public static RatingResponse createRatingResponse() {
        return new RatingResponse(id, driverId, passengerId, rideId, score, comment, whoRate, isDeleted);
    }

    public static RatingRequest createRatingRequest() {
        return new RatingRequest(rideId, score, comment, whoRate);
    }

    public static GetRideResponse createRideResponse() {
        return new GetRideResponse(rideId, driverId, passengerId);
    }
}
