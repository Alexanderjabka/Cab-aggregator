package org.internship.task.rideservice.repositories;

import java.util.List;
import org.internship.task.rideservice.entities.Ride;
import org.internship.task.rideservice.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findAllByStatusOrderByIdAsc(Status status);

    List<Ride> findAllByOrderByIdAsc();

    boolean existsByPassengerIdAndStatusIn(Long passengerId, List<Status> activeStatuses);

    List<Ride> findByPassengerId(Long id);

}
