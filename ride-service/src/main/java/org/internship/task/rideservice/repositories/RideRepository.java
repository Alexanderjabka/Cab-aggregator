package org.internship.task.rideservice.repositories;

import java.util.List;
import java.util.Optional;
import org.internship.task.rideservice.entities.Ride;
import org.internship.task.rideservice.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findAllByStatus(Status status);

    Optional<Ride> findByPassengerId(Long id);

    Optional<Ride> findByDriverId(Long id);
}
