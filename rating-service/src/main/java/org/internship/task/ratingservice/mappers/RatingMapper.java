package org.internship.task.ratingservice.mappers;

import java.util.List;
import org.internship.task.ratingservice.dto.RatingRequest;
import org.internship.task.ratingservice.dto.RatingResponse;
import org.internship.task.ratingservice.entities.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "whoRate", source = "whoRate")
    Rating ratingRequestToRating(RatingRequest ratingRequest);

    RatingResponse ratingToRatingResponse(Rating rating);

    List<RatingResponse> toDtoList(List<Rating> ratings);

}
