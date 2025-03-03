package org.internship.task.ratingservice.controllers;

import static org.internship.task.ratingservice.util.TestDataForRatingTests.createRatingRequest;
import static org.internship.task.ratingservice.util.TestDataForRatingTests.createRatingResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.internship.task.ratingservice.dto.RatingListResponse;
import org.internship.task.ratingservice.dto.RatingRequest;
import org.internship.task.ratingservice.dto.RatingResponse;
import org.internship.task.ratingservice.services.RatingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class RatingControllerTest {

    @Mock
    private RatingServiceImpl ratingService;

    @InjectMocks
    private RatingController ratingController;

    private RatingRequest ratingRequest;
    private RatingResponse ratingResponse;

    @BeforeEach
    void setUp() {
        ratingRequest = createRatingRequest();
        ratingResponse = createRatingResponse();
    }

    @Test
    void getAllRatings_ShouldReturnNoContentWhenNoRatingsExist() {
        when(ratingService.getAllRatings()).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<RatingListResponse> response = ratingController.getAllRatings();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ratingService, times(1)).getAllRatings();
    }

    @Test
    void createRating_ShouldReturnRatingResponse() {
        when(ratingService.createRating(ratingRequest)).thenReturn(ratingResponse);

        ResponseEntity<RatingResponse> response = ratingController.createRating(ratingRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ratingResponse, response.getBody());
        verify(ratingService, times(1)).createRating(ratingRequest);
    }

    @Test
    void getAveragePassengerRating_ShouldReturnAverageRating() {
        when(ratingService.getAveragePassengerRating(1L)).thenReturn(4.5);

        ResponseEntity<Double> response = ratingController.getAveragePassengerRating(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4.5, response.getBody());
        verify(ratingService, times(1)).getAveragePassengerRating(1L);
    }

    @Test
    void getAverageDriverRating_ShouldReturnAverageRating() {
        when(ratingService.getAverageDriverRating(1L)).thenReturn(4.8);

        ResponseEntity<Double> response = ratingController.getAverageDriverRating(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4.8, response.getBody());
        verify(ratingService, times(1)).getAverageDriverRating(1L);
    }

    @Test
    void deleteRating_ShouldReturnNoContent() {
        when(ratingService.deleteRating(1L)).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<String> response = ratingController.deleteRating(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ratingService, times(1)).deleteRating(1L);
    }
}