package org.internship.task.passengerservice.repositories;

import org.internship.task.passengerservice.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger,Long> {
    Optional<Passenger> findByEmail(String email);
    List<Passenger> findByIsDeleted(Boolean isDeleted);
}
