package org.internship.task.driverservice.repositories;

import java.util.List;
import java.util.Optional;
import org.internship.task.driverservice.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);

    List<Driver> findByIsDeletedOrderByIdAsc(Boolean isDeleted);

    List<Driver> findAllByOrderByIdAsc();

    Optional<Driver> findFirstByIsInRideFalseAndIsDeletedFalseAndCarsIsDeletedFalseOrderByIdAsc();

}
