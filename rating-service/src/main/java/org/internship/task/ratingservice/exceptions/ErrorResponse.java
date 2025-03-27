package org.internship.task.ratingservice.exceptions;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ErrorResponse(
    int status,
    String error,
    String message,
    LocalDateTime timestamp
) {
    public static ErrorResponse of(int status, String error, String message) {
        return ErrorResponse.builder()
            .status(status)
            .error(error)
            .message(message)
            .timestamp(LocalDateTime.now())
            .build();
    }
}