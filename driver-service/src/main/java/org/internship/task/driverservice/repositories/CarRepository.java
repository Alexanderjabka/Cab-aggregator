package org.internship.task.driverservice.repositories;

import org.internship.task.driverservice.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByCarNumber(String carNumber);

    List<Car> findByIsDeletedOrderByIdAsc(Boolean isDeleted);

    List<Car> findAllByOrderByIdAsc();
}
