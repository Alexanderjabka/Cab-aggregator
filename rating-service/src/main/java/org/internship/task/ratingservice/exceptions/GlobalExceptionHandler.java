package org.internship.task.ratingservice.exceptions;

import org.internship.task.ratingservice.exceptions.ratingExceptions.InvalidRatingOperationException;
import org.internship.task.ratingservice.exceptions.ratingExceptions.RatingNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RatingNotFoundException.class)
    public ResponseEntity<String> handleRatingNotFoundException(RatingNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidRatingOperationException.class)
    public ResponseEntity<String> handleInvalidRatingOperationException(InvalidRatingOperationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}