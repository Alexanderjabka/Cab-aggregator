package org.internship.task.ratingservice.services;

import static org.internship.task.ratingservice.util.constantMessages.exceptionRatingMessages.RatingExceptionMessages.IS_ALREADY_RATE_THIS_RIDE;
import static org.internship.task.ratingservice.util.constantMessages.exceptionRatingMessages.RatingExceptionMessages.THIS_PERSON_DOESNT_HAVE_RATING_YET;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.internship.task.ratingservice.dto.RatingRequest;
import org.internship.task.ratingservice.dto.RatingResponse;
import org.internship.task.ratingservice.entities.Rating;
import org.internship.task.ratingservice.enums.WhoRate;
import org.internship.task.ratingservice.exceptions.ratingExceptions.InvalidRatingOperationException;
import org.internship.task.ratingservice.exceptions.ratingExceptions.RatingNotFoundException;
import org.internship.task.ratingservice.mappers.RatingMapper;
import org.internship.task.ratingservice.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    @Value("${rating.recent-limit}")
    private int recentLimit;

    @Override
    public double getAveragePassengerRating(Long passengerId) {
        Pageable pageable = PageRequest.of(0, recentLimit, Sort.by(Sort.Direction.DESC, "id"));

        List<Rating> ratings = ratingRepository.findLastRatingsForPassenger(passengerId, WhoRate.DRIVER, pageable);

        return calculateAverage(ratings);
    }

    @Override
    public double getAverageDriverRating(Long driverId) {
        Pageable pageable = PageRequest.of(0, recentLimit, Sort.by(Sort.Direction.DESC, "id"));
        List<Rating> ratings = ratingRepository.findLastRatingsForDriver(driverId, WhoRate.PASSENGER, pageable);

        return calculateAverage(ratings);
    }

    private double calculateAverage(List<Rating> ratings) {
        if (ratings.isEmpty()) {
            throw new RatingNotFoundException(THIS_PERSON_DOESNT_HAVE_RATING_YET);
        }
        return ratings.stream().mapToInt(Rating::getScore).average().orElse(0.0);
    }

    @Transactional
    @Override
    public RatingResponse createRating(RatingRequest ratingRequest) {
        Rating rating = ratingMapper.ratingRequestToRating(ratingRequest);

        if (ratingRepository.findByRideIdAndWhoRate(rating.getRideId(), rating.getWhoRate()).isPresent()) {
            throw new InvalidRatingOperationException(rating.getWhoRate() + IS_ALREADY_RATE_THIS_RIDE);
        }

        Rating savedRating = ratingRepository.save(rating);

        return ratingMapper.ratingToRatingResponse(savedRating);
    }
}
