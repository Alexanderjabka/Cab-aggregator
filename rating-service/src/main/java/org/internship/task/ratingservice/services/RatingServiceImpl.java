package org.internship.task.ratingservice.services;

import static org.internship.task.ratingservice.util.constantMessages.exceptionRatingMessages.RatingExceptionMessages.ALL_MEMBERS_ARE_ALREADY_RATE_THIS_RIDE;
import static org.internship.task.ratingservice.util.constantMessages.exceptionRatingMessages.RatingExceptionMessages.IS_ALREADY_RATE_THIS_RIDE;
import static org.internship.task.ratingservice.util.constantMessages.exceptionRatingMessages.RatingExceptionMessages.RATING_IS_NOT_FOUND_BY_ID;
import static org.internship.task.ratingservice.util.constantMessages.exceptionRatingMessages.RatingExceptionMessages.THIS_PERSON_DOESNT_HAVE_RATING_YET;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.internship.task.ratingservice.dto.RatingListResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    @Setter
    @Value("${rating.recent-limit}")
    private int recentLimit;

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<RatingListResponse> getAllRatings() {
        List<Rating> ratings = ratingRepository.findAllByOrderByIdAsc();

        return ratings.isEmpty() ? ResponseEntity.noContent().build() :
            ResponseEntity.ok(RatingListResponse.builder().ratings(ratingMapper.toDtoList(ratings)).build());
    }

    @Transactional(readOnly = true)
    @Override
    public double getAveragePassengerRating(Long passengerId) {
        Pageable pageable = PageRequest.of(0, recentLimit, Sort.by(Sort.Direction.DESC, "id"));

        List<Rating> ratings =
            ratingRepository.findByPassengerIdAndWhoRateAndIsDeletedFalseOrderByIdDesc(passengerId, WhoRate.DRIVER,
                pageable);

        return calculateAverage(ratings);
    }

    @Transactional(readOnly = true)
    @Override
    public double getAverageDriverRating(Long driverId) {
        Pageable pageable = PageRequest.of(0, recentLimit, Sort.by(Sort.Direction.DESC, "id"));
        List<Rating> ratings =
            ratingRepository.findByDriverIdAndWhoRateAndIsDeletedFalseOrderByIdDesc(driverId, WhoRate.PASSENGER,
                pageable);

        return calculateAverage(ratings);
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteRating(Long ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
            .orElseThrow(() -> new RatingNotFoundException(RATING_IS_NOT_FOUND_BY_ID + ratingId));

        rating.setIsDeleted(true);

        ratingRepository.save(rating);

        return ResponseEntity.noContent().build();
    }

    @Transactional
    @Override
    public RatingResponse createRating(RatingRequest ratingRequest) {
        List<Rating> rideRatings = ratingRepository.findAllByRideIdAndIsDeletedFalse(ratingRequest.getRideId());
        if (rideRatings.isEmpty()) {
            throw new RatingNotFoundException(RATING_IS_NOT_FOUND_BY_ID + ratingRequest.getRideId());
        }
        if (rideRatings.size() == 2) {
            throw new InvalidRatingOperationException(ALL_MEMBERS_ARE_ALREADY_RATE_THIS_RIDE);
        }
        if (ratingRepository.findByRideIdAndWhoRateAndIsDeletedFalse(ratingRequest.getRideId(),
            ratingRequest.getWhoRate()).isPresent()) {
            throw new InvalidRatingOperationException(ratingRequest.getWhoRate() + IS_ALREADY_RATE_THIS_RIDE);
        }

        Rating existingRating = rideRatings.get(0);
        if (existingRating.getScore() == null) {
            existingRating.setScore(ratingRequest.getScore());
            existingRating.setComment(ratingRequest.getComment());
            existingRating.setWhoRate(ratingRequest.getWhoRate());
        } else {
            Rating newRating = new Rating();
            newRating.setRideId(ratingRequest.getRideId());
            newRating.setPassengerId(existingRating.getPassengerId());
            newRating.setDriverId(existingRating.getDriverId());
            newRating.setScore(ratingRequest.getScore());
            newRating.setComment(ratingRequest.getComment());
            newRating.setWhoRate(ratingRequest.getWhoRate());
            newRating.setIsDeleted(false);

            Rating savedRating = ratingRepository.save(newRating);
            return ratingMapper.ratingToRatingResponse(savedRating);
        }

        Rating savedRating = ratingRepository.save(existingRating);
        return ratingMapper.ratingToRatingResponse(savedRating);
    }

    private double calculateAverage(List<Rating> ratings) {
        if (ratings.isEmpty()) {
            throw new RatingNotFoundException(THIS_PERSON_DOESNT_HAVE_RATING_YET);
        }
        return ratings.stream().mapToInt(Rating::getScore).average().orElse(0.0);
    }
}
