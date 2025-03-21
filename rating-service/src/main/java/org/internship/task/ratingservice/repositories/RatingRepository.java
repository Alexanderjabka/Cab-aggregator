package org.internship.task.ratingservice.repositories;

import org.internship.task.ratingservice.entities.Rating;
import org.internship.task.ratingservice.enums.WhoRate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByRideIdAndWhoRateAndIsDeletedFalse(Long rideId, WhoRate whoRate);

    List<Rating> findAllByOrderByIdAsc();

    List<Rating> findByPassengerIdAndWhoRateAndIsDeletedFalseOrderByIdDesc(Long passengerId, WhoRate whoRate,
                                                                           Pageable pageable);

    List<Rating> findByDriverIdAndWhoRateAndIsDeletedFalseOrderByIdDesc(Long driverId, WhoRate whoRate,
                                                                        Pageable pageable);

}
