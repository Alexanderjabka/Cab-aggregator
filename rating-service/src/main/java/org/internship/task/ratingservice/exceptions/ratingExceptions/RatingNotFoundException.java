package org.internship.task.ratingservice.exceptions.ratingExceptions;

public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException(String message) {
        super(message);
    }
}
