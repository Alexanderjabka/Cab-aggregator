package org.internship.task.ratingservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RatingListResponse(
        List<RatingResponse> ratings
) {
}
