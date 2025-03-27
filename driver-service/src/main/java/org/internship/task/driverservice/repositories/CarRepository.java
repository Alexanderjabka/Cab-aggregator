package org.internship.task.driverservice.repositories;

import java.util.List;
import java.util.Optional;
import org.internship.task.driverservice.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByCarNumber(String carNumber);

    List<Car> findByIsDeletedOrderByIdAsc(Boolean isDeleted);

    List<Car> findAllByOrderByIdAsc();
}
