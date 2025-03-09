package org.internship.task.ratingservice.TestDataIT;

import java.util.List;
import org.internship.task.ratingservice.dto.RatingRequest;
import org.internship.task.ratingservice.dto.RatingResponse;
import org.internship.task.ratingservice.dto.clientsDto.GetRideResponse;
import org.internship.task.ratingservice.entities.Rating;
import org.internship.task.ratingservice.enums.WhoRate;

public class DataForIT {

    public static final Long ID = 1L;
    public static final Long RIDE_ID = 1L;
    public static final Long DRIVER_ID = 1L;
    public static final Long PASSENGER_ID = 1L;
    public static final Long INVALID_ID = 999L;

    public static final Short SCORE = 5;
    public static final String COMMENT = "Great ride!";
    public static final WhoRate WHO_RATE = WhoRate.PASSENGER;

    public static final Boolean IS_DELETED = false;

    public static final GetRideResponse RIDE_RESPONSE = new GetRideResponse(RIDE_ID, DRIVER_ID, PASSENGER_ID);
    public static final GetRideResponse INVALID_RIDE_RESPONSE =
        new GetRideResponse(INVALID_ID, DRIVER_ID, PASSENGER_ID);

    public static final RatingRequest RATING_REQUEST = new RatingRequest(RIDE_ID, SCORE, COMMENT, WHO_RATE);
    public static final RatingResponse RATING_RESPONSE =
        new RatingResponse(ID, DRIVER_ID, PASSENGER_ID, RIDE_ID, SCORE, COMMENT, WHO_RATE,
            false);

    public static final List<Rating> RATINGS = List.of(
        new Rating(ID, DRIVER_ID, PASSENGER_ID, ID, SCORE, COMMENT, WHO_RATE, IS_DELETED)
    );
    public static final List<Rating> PASSENGER_RATINGS = List.of(
        new Rating(ID, DRIVER_ID, PASSENGER_ID, ID, SCORE, COMMENT, WhoRate.DRIVER, IS_DELETED)
    );

    public static final Rating CREATE_RATING() {
        return new Rating(ID, DRIVER_ID, PASSENGER_ID, ID, SCORE, COMMENT, WHO_RATE, IS_DELETED);
    }
}