package org.internship.task.passengerservice.repositories;

import java.util.List;
import java.util.Optional;
import org.internship.task.passengerservice.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByEmail(String email);

    List<Passenger> findByIsDeletedOrderByIdAsc(Boolean isDeleted);

    List<Passenger> findAllByOrderByIdAsc();
}
