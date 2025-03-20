package org.internship.task.passengerservice.repositories;

import org.internship.task.passengerservice.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByEmail(String email);

    Optional<Passenger> findByIdAndIsDeletedFalse(Long id);

    List<Passenger> findByIsDeletedOrderByIdAsc(Boolean isDeleted);

    List<Passenger> findAllByOrderByIdAsc();
}
