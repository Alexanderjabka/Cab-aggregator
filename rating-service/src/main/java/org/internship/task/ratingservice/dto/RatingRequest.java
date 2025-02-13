package org.internship.task.ratingservice.dto;

import static org.internship.task.ratingservice.util.constantMessages.validationRatingMessages.RatingValidationMessages.COMMENT_MUST_BE_BETWEEN_3_AND_255;
import static org.internship.task.ratingservice.util.constantMessages.validationRatingMessages.RatingValidationMessages.DRIVER_ID_CANNOT_BE_NULL;
import static org.internship.task.ratingservice.util.constantMessages.validationRatingMessages.RatingValidationMessages.FIELD_WHO_RATE_CANNOT_BE_NULL;
import static org.internship.task.ratingservice.util.constantMessages.validationRatingMessages.RatingValidationMessages.IS_DELETED_CANNOT_BE_NULL;
import static org.internship.task.ratingservice.util.constantMessages.validationRatingMessages.RatingValidationMessages.MAXIMUM_SCORE_IS_5;
import static org.internship.task.ratingservice.util.constantMessages.validationRatingMessages.RatingValidationMessages.MINIMAL_SCORE_IS_0;
import static org.internship.task.ratingservice.util.constantMessages.validationRatingMessages.RatingValidationMessages.PASSENGER_ID_CANNOT_BE_NULL;
import static org.internship.task.ratingservice.util.constantMessages.validationRatingMessages.RatingValidationMessages.RIDE_ID_CANNOT_BE_NULL;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.internship.task.ratingservice.enums.WhoRate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequest {
    @NotNull(message = PASSENGER_ID_CANNOT_BE_NULL)
    private Long passengerId;

    @NotNull(message = DRIVER_ID_CANNOT_BE_NULL)
    private Long driverId;

    @Min(value = 0, message = MINIMAL_SCORE_IS_0)
    @Max(value = 5, message = MAXIMUM_SCORE_IS_5)
    private Short score;

    @Size(min = 3, max = 255, message = COMMENT_MUST_BE_BETWEEN_3_AND_255)
    private String comment;

    @NotNull(message = FIELD_WHO_RATE_CANNOT_BE_NULL)
    private WhoRate whoRate;

    @NotNull(message = RIDE_ID_CANNOT_BE_NULL)
    private Long rideId;
}
