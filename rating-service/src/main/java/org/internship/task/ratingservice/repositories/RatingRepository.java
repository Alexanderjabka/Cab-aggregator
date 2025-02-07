package org.internship.task.ratingservice.repositories;

import java.util.List;
import java.util.Optional;
import org.internship.task.ratingservice.entities.Rating;
import org.internship.task.ratingservice.enums.WhoRate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByRideIdAndWhoRate(Long rideId, WhoRate whoRate);

    @Query("SELECT r FROM Rating r WHERE r.passengerId = :passengerId AND r.whoRate = :whoRate ORDER BY r.id DESC")
    List<Rating> findLastRatingsForPassenger(@Param("passengerId") Long passengerId, @Param("whoRate") WhoRate whoRate, Pageable pageable);

    @Query("SELECT r FROM Rating r WHERE r.driverId = :driverId AND r.whoRate = :whoRate ORDER BY r.id DESC")
    List<Rating> findLastRatingsForDriver(@Param("driverId") Long driverId, @Param("whoRate") WhoRate whoRate, Pageable pageable);
}
