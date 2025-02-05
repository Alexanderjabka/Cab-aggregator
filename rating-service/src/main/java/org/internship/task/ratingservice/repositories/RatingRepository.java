package org.internship.task.ratingservice.repositories;

import org.internship.task.ratingservice.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}
