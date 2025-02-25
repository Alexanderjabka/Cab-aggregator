package org.internship.task.rideservice.exceptions.feignExceptions;

import lombok.Getter;
import org.internship.task.rideservice.exceptions.ErrorResponse;


@Getter
public class FeignClientException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public FeignClientException(ErrorResponse apiResponse) {
        super(apiResponse.message());
        this.errorResponse = apiResponse;
    }
}
