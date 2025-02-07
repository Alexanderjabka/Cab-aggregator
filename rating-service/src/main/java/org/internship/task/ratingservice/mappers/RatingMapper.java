package org.internship.task.ratingservice.mappers;

import org.internship.task.ratingservice.dto.RatingRequest;
import org.internship.task.ratingservice.dto.RatingResponse;
import org.internship.task.ratingservice.entities.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    @Mapping(target = "whoRate", source = "whoRate")
    @Mapping(target = "rideId", source = "rideId")
    Rating ratingRequestToRating(RatingRequest ratingRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "driverId", source = "driverId")
    @Mapping(target = "passengerId", source = "passengerId")
    @Mapping(target = "rideId", source = "rideId")
    @Mapping(target = "score", source = "score")
    @Mapping(target = "comment", source = "comment")
    RatingResponse ratingToRatingResponse(Rating rating);
}
