package org.internship.task.ratingservice.services;

import org.internship.task.ratingservice.clients.RideClient;
import org.internship.task.ratingservice.dto.RatingListResponse;
import org.internship.task.ratingservice.dto.RatingRequest;
import org.internship.task.ratingservice.dto.RatingResponse;
import org.internship.task.ratingservice.dto.clientsDto.GetRideResponse;
import org.internship.task.ratingservice.entities.Rating;
import org.internship.task.ratingservice.enums.WhoRate;
import org.internship.task.ratingservice.exceptions.ratingExceptions.InvalidRatingOperationException;
import org.internship.task.ratingservice.exceptions.ratingExceptions.RatingNotFoundException;
import org.internship.task.ratingservice.mappers.RatingMapper;
import org.internship.task.ratingservice.repositories.RatingRepository;
import org.internship.task.ratingservice.util.TestDataForRatingTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.internship.task.ratingservice.util.constantMessages.exceptionRatingMessages.RatingExceptionMessages.THIS_PERSON_DOESNT_HAVE_RATING_YET;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingMapper ratingMapper;

    @Mock
    private RideClient rideClient;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private RatingRequest ratingRequest;
    private RatingResponse ratingResponse;
    private Rating rating;
    private GetRideResponse rideResponse;

    private Pageable pageable;


    @BeforeEach
    void setUp() {
        ratingService = new RatingServiceImpl(ratingRepository, ratingMapper, rideClient);
        ratingService.setRecentLimit(10);

        ratingRequest = TestDataForRatingTests.createRatingRequest();
        ratingResponse = TestDataForRatingTests.createRatingResponse();
        rating = TestDataForRatingTests.createRating();
        rideResponse = TestDataForRatingTests.createRideResponse();
        pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

    }

    @Test
    void getAllRatings_ShouldReturnNoContentWhenNoRatingsExist() {
        when(ratingRepository.findAllByOrderByIdAsc()).thenReturn(Collections.emptyList());

        ResponseEntity<RatingListResponse> response = ratingService.getAllRatings();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ratingRepository).findAllByOrderByIdAsc();
    }

    @Test
    void getAllRatings_ShouldReturnRatingsWhenRatingsExist() {
        when(ratingRepository.findAllByOrderByIdAsc()).thenReturn(List.of(rating));
        when(ratingMapper.toDtoList(List.of(rating))).thenReturn(List.of(ratingResponse));

        ResponseEntity<RatingListResponse> response = ratingService.getAllRatings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().ratings().size());
        verify(ratingRepository).findAllByOrderByIdAsc();
    }

    @Test
    void getAveragePassengerRating_ShouldReturnAverageRating() {
        when(ratingRepository.findByPassengerIdAndWhoRateAndIsDeletedFalseOrderByIdDesc(1L, WhoRate.DRIVER, pageable))
                .thenReturn(List.of(rating));

        double averageRating = ratingService.getAveragePassengerRating(1L);

        assertEquals(5.0, averageRating);
        verify(ratingRepository)
                .findByPassengerIdAndWhoRateAndIsDeletedFalseOrderByIdDesc(1L, WhoRate.DRIVER, pageable);
    }

    @Test
    void getAveragePassengerRating_ShouldThrowExceptionWhenNoRatingsExist() {
        when(ratingRepository.findByPassengerIdAndWhoRateAndIsDeletedFalseOrderByIdDesc(1L, WhoRate.DRIVER, pageable))
                .thenReturn(Collections.emptyList());

        RatingNotFoundException exception = assertThrows(RatingNotFoundException.class, () -> {
            ratingService.getAveragePassengerRating(1L);
        });

        assertEquals(THIS_PERSON_DOESNT_HAVE_RATING_YET, exception.getMessage());
        verify(ratingRepository)
                .findByPassengerIdAndWhoRateAndIsDeletedFalseOrderByIdDesc(1L, WhoRate.DRIVER, pageable);
    }

    @Test
    void getAverageDriverRating_ShouldReturnAverageRating() {
        when(ratingRepository.findByDriverIdAndWhoRateAndIsDeletedFalseOrderByIdDesc(1L, WhoRate.PASSENGER, pageable))
                .thenReturn(List.of(rating));

        double averageRating = ratingService.getAverageDriverRating(1L);

        assertEquals(5.0, averageRating);
        verify(ratingRepository)
                .findByDriverIdAndWhoRateAndIsDeletedFalseOrderByIdDesc(1L, WhoRate.PASSENGER, pageable);
    }

    @Test
    void getAverageDriverRating_ShouldThrowExceptionWhenNoRatingsExist() {
        when(ratingRepository.findByDriverIdAndWhoRateAndIsDeletedFalseOrderByIdDesc(1L, WhoRate.PASSENGER, pageable))
                .thenReturn(Collections.emptyList());

        RatingNotFoundException exception = assertThrows(RatingNotFoundException.class, () -> {
            ratingService.getAverageDriverRating(1L);
        });

        assertEquals(THIS_PERSON_DOESNT_HAVE_RATING_YET, exception.getMessage());
        verify(ratingRepository)
                .findByDriverIdAndWhoRateAndIsDeletedFalseOrderByIdDesc(1L, WhoRate.PASSENGER, pageable);
    }

    @Test
    void deleteRating_ShouldMarkRatingAsDeleted() {
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating));

        ResponseEntity<String> response = ratingService.deleteRating(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(rating.getIsDeleted());
        verify(ratingRepository).save(rating);
    }

    @Test
    void createRating_ShouldCreateRatingWhenRideIsNotRatedYet() {
        when(rideClient.getRideByIdAndAbilityToRate(ratingRequest.getRideId())).thenReturn(rideResponse);
        when(ratingMapper.ratingRequestToRating(ratingRequest)).thenReturn(rating);
        rating.setPassengerId(rideResponse.getPassengerId());
        rating.setDriverId(rideResponse.getDriverId());
        rating.setIsDeleted(false);
        when(ratingRepository.save(rating)).thenReturn(rating);
        when(ratingMapper.ratingToRatingResponse(rating)).thenReturn(ratingResponse);

        RatingResponse response = ratingService.createRating(ratingRequest);

        assertNotNull(response);
        assertEquals(ratingResponse, response);
        verify(ratingRepository).save(rating);
    }

    @Test
    void createRating_ShouldThrowExceptionWhenRideIsAlreadyRated() {
        when(rideClient.getRideByIdAndAbilityToRate(1L)).thenReturn(rideResponse);
        when(ratingRepository.findByRideIdAndWhoRateAndIsDeletedFalse(1L, WhoRate.DRIVER))
                .thenReturn(Optional.of(rating));

        assertThrows(InvalidRatingOperationException.class, () -> ratingService.createRating(ratingRequest));
    }
}