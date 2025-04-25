package org.internship.task.ratingservice.services;

import static org.internship.task.ratingservice.util.constantMessages.exceptionRatingMessages.RatingExceptionMessages.ALL_MEMBERS_ARE_ALREADY_RATE_THIS_RIDE;
import static org.internship.task.ratingservice.util.constantMessages.exceptionRatingMessages.RatingExceptionMessages.IS_ALREADY_RATE_THIS_RIDE;
import static org.internship.task.ratingservice.util.constantMessages.exceptionRatingMessages.RatingExceptionMessages.THIS_PERSON_DOESNT_HAVE_RATING_YET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.internship.task.ratingservice.dto.RatingListResponse;
import org.internship.task.ratingservice.dto.RatingRequest;
import org.internship.task.ratingservice.dto.RatingResponse;
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

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingMapper ratingMapper;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private RatingRequest ratingRequest;
    private RatingResponse ratingResponse;
    private Rating rating;

    private Pageable pageable;


    @BeforeEach
    void setUp() {
        ratingService = new RatingServiceImpl(ratingRepository, ratingMapper);
        ratingService.setRecentLimit(10);

        ratingRequest = TestDataForRatingTests.createRatingRequest();
        ratingResponse = TestDataForRatingTests.createRatingResponse();
        rating = TestDataForRatingTests.createRating();
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
        // Arrange
        RatingRequest ratingRequest = new RatingRequest(1L, (short) 5, "Great ride!", WhoRate.DRIVER);

        // Существующий рейтинг (который будет обновлен)
        Rating existingRating = new Rating();
        existingRating.setRideId(1L);
        existingRating.setPassengerId(2L);
        existingRating.setDriverId(3L);
        existingRating.setScore(null);
        existingRating.setWhoRate(null);
        existingRating.setIsDeleted(false);

        // Ожидаемый сохраненный рейтинг после обновления
        Rating savedRating = new Rating();
        savedRating.setRideId(1L);
        savedRating.setPassengerId(2L);
        savedRating.setDriverId(3L);
        savedRating.setScore((short) 5);
        savedRating.setComment("Great ride!");
        savedRating.setWhoRate(WhoRate.DRIVER);
        savedRating.setIsDeleted(false);

        RatingResponse expectedResponse =
            new RatingResponse(1L, 1L, 2L, 3L, (short) 5, "Great ride!", WhoRate.DRIVER, false);

        when(ratingRepository.findAllByRideIdAndIsDeletedFalse(1L))
            .thenReturn(List.of(existingRating));
        when(ratingMapper.ratingToRatingResponse(savedRating))
            .thenReturn(expectedResponse);
        when(ratingRepository.save(existingRating)).thenReturn(savedRating);

        // Act
        RatingResponse response = ratingService.createRating(ratingRequest);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(ratingRepository).save(existingRating);
    }

    @Test
    void createRating_ShouldUpdateEmptyRatingWhenExists() {
        // Arrange
        RatingRequest request = new RatingRequest(1L, (short) 5, "Great!", WhoRate.DRIVER);

        // Пустая запись (созданная Kafka)
        Rating emptyRating = new Rating();
        emptyRating.setId(1L);
        emptyRating.setRideId(1L);
        emptyRating.setPassengerId(2L);
        emptyRating.setDriverId(3L);
        emptyRating.setScore(null);
        emptyRating.setComment(null);
        emptyRating.setWhoRate(null);
        emptyRating.setIsDeleted(false);

        // Ожидаемое состояние после обновления
        Rating expectedSaved = new Rating();
        expectedSaved.setId(1L);
        expectedSaved.setRideId(1L);
        expectedSaved.setPassengerId(2L);
        expectedSaved.setDriverId(3L);
        expectedSaved.setScore((short) 5);
        expectedSaved.setComment("Great!");
        expectedSaved.setWhoRate(WhoRate.DRIVER);
        expectedSaved.setIsDeleted(false);

        RatingResponse expectedResponse =
            new RatingResponse(1L, 1L, 2L, 3L, (short) 5, "Great!", WhoRate.DRIVER, false);

        when(ratingRepository.findAllByRideIdAndIsDeletedFalse(1L))
            .thenReturn(List.of(emptyRating));
        when(ratingRepository.save(emptyRating)).thenReturn(expectedSaved);
        when(ratingMapper.ratingToRatingResponse(expectedSaved)).thenReturn(expectedResponse);

        // Act
        RatingResponse response = ratingService.createRating(request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(ratingRepository).save(emptyRating);
        assertEquals("Great!", emptyRating.getComment());
        assertEquals(WhoRate.DRIVER, emptyRating.getWhoRate());
    }

    @Test
    void createRating_ShouldCreateNewRatingWhenFirstExists() {
        // Arrange
        RatingRequest request = new RatingRequest(1L, (short) 4, "Good", WhoRate.PASSENGER);

        // Существующая оценка водителя
        Rating driverRating = new Rating();
        driverRating.setId(1L);
        driverRating.setRideId(1L);
        driverRating.setPassengerId(2L);
        driverRating.setDriverId(3L);
        driverRating.setScore((short) 5);
        driverRating.setComment("Excellent");
        driverRating.setWhoRate(WhoRate.DRIVER);
        driverRating.setIsDeleted(false);

        // Новая запись для пассажира
        Rating newRating = new Rating();
        newRating.setRideId(1L);
        newRating.setPassengerId(2L);
        newRating.setDriverId(3L);
        newRating.setScore((short) 4);
        newRating.setComment("Good");
        newRating.setWhoRate(WhoRate.PASSENGER);
        newRating.setIsDeleted(false);

        // Ожидаемый сохраненный объект
        Rating savedRating = new Rating();
        savedRating.setId(2L);
        savedRating.setRideId(1L);
        savedRating.setPassengerId(2L);
        savedRating.setDriverId(3L);
        savedRating.setScore((short) 4);
        savedRating.setComment("Good");
        savedRating.setWhoRate(WhoRate.PASSENGER);
        savedRating.setIsDeleted(false);

        RatingResponse expectedResponse =
            new RatingResponse(2L, 1L, 2L, 3L, (short) 4, "Good", WhoRate.PASSENGER, false);

        when(ratingRepository.findAllByRideIdAndIsDeletedFalse(1L))
            .thenReturn(List.of(driverRating));
        when(ratingRepository.save(newRating)).thenReturn(savedRating);
        when(ratingMapper.ratingToRatingResponse(savedRating)).thenReturn(expectedResponse);

        // Act
        RatingResponse response = ratingService.createRating(request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(ratingRepository).save(newRating);
    }

    @Test
    void createRating_ShouldThrowWhenDuplicateRating() {
        // Arrange
        RatingRequest request = new RatingRequest(1L, (short) 3, "Average", WhoRate.DRIVER);

        Rating existingRating = new Rating();
        existingRating.setId(1L);
        existingRating.setRideId(1L);
        existingRating.setPassengerId(2L);
        existingRating.setDriverId(3L);
        existingRating.setScore((short) 5);
        existingRating.setComment("Excellent");
        existingRating.setWhoRate(WhoRate.DRIVER);
        existingRating.setIsDeleted(false);

        when(ratingRepository.findAllByRideIdAndIsDeletedFalse(1L))
            .thenReturn(List.of(existingRating));
        when(ratingRepository.findByRideIdAndWhoRateAndIsDeletedFalse(1L, WhoRate.DRIVER))
            .thenReturn(Optional.of(existingRating));

        // Act & Assert
        InvalidRatingOperationException exception = assertThrows(
            InvalidRatingOperationException.class,
            () -> ratingService.createRating(request)
        );

        assertEquals(WhoRate.DRIVER + IS_ALREADY_RATE_THIS_RIDE, exception.getMessage());
    }

    @Test
    void createRating_ShouldThrowWhenAllMembersRated() {
        // Arrange
        RatingRequest request = new RatingRequest(1L, (short) 3, "Average", WhoRate.DRIVER);

        Rating driverRating = new Rating();
        driverRating.setId(1L);
        driverRating.setRideId(1L);
        driverRating.setPassengerId(2L);
        driverRating.setDriverId(3L);
        driverRating.setScore((short) 5);
        driverRating.setComment("Excellent");
        driverRating.setWhoRate(WhoRate.DRIVER);
        driverRating.setIsDeleted(false);

        Rating passengerRating = new Rating();
        passengerRating.setId(2L);
        passengerRating.setRideId(1L);
        passengerRating.setPassengerId(2L);
        passengerRating.setDriverId(3L);
        passengerRating.setScore((short) 4);
        passengerRating.setComment("Good");
        passengerRating.setWhoRate(WhoRate.PASSENGER);
        passengerRating.setIsDeleted(false);

        when(ratingRepository.findAllByRideIdAndIsDeletedFalse(1L))
            .thenReturn(List.of(driverRating, passengerRating));

        // Act & Assert
        InvalidRatingOperationException exception = assertThrows(
            InvalidRatingOperationException.class,
            () -> ratingService.createRating(request)
        );

        assertEquals(ALL_MEMBERS_ARE_ALREADY_RATE_THIS_RIDE, exception.getMessage());
    }
}