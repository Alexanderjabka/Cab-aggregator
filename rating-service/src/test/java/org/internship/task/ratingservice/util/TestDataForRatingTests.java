package org.internship.task.ratingservice.util;

import org.internship.task.ratingservice.dto.RatingRequest;
import org.internship.task.ratingservice.dto.RatingResponse;
import org.internship.task.ratingservice.entities.Rating;
import org.internship.task.ratingservice.enums.WhoRate;

public class TestDataForRatingTests {
    private static final Long ID = 1L;

    private static final Long RIDE_ID = 1L;

    private static final Long DRIVER_ID = 1L;

    private static final Long PASSENGER_ID = 1L;

    private static final Short SCORE = 5;

    private static final String COMMENT = "Good";

    private static final WhoRate WHO_RATE = WhoRate.DRIVER;

    private static final Boolean IS_DELETED = false;

    public static Rating createRating() {
        return new Rating(ID, RIDE_ID, DRIVER_ID, PASSENGER_ID, SCORE, COMMENT, WHO_RATE, IS_DELETED);
    }

    public static RatingResponse createRatingResponse() {
        return new RatingResponse(ID, DRIVER_ID, PASSENGER_ID, RIDE_ID, SCORE, COMMENT, WHO_RATE, IS_DELETED);
    }

    public static RatingRequest createRatingRequest() {
        return new RatingRequest(RIDE_ID, SCORE, COMMENT, WHO_RATE);
    }

}
