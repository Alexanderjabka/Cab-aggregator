package org.internship.task.passengerservice.repositories;

import org.internship.task.passengerservice.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger,Long> {
    Optional<Passenger> findByName(String name);
    Optional<Passenger> findByEmail(String email);

}
