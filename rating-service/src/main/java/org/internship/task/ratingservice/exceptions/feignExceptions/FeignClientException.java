package org.internship.task.ratingservice.exceptions.feignExceptions;

import lombok.Getter;
import org.internship.task.ratingservice.exceptions.ErrorResponse;

@Getter
public class FeignClientException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public FeignClientException(ErrorResponse apiResponse) {
        super(apiResponse.message());
        this.errorResponse = apiResponse;
    }
}
