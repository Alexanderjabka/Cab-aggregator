package org.internship.task.rideservice.repositories;

import org.internship.task.rideservice.entities.Ride;
import org.internship.task.rideservice.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride,Long> {
    List<Ride> findAllByStatus(Status status);
    Optional<Long> findByPassengerId(Long id);
}
