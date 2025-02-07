package org.internship.task.ratingservice.exceptions.ratingExceptions;

public class InvalidRatingOperationException extends RuntimeException {
    public InvalidRatingOperationException(String message) {
        super(message);
    }
}
