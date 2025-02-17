package org.internship.task.ratingservice.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record RatingListResponse(
        List<RatingResponse> ratings
) {
}
