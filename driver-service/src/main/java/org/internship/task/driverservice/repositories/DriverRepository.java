package org.internship.task.driverservice.repositories;

import java.util.List;
import java.util.Optional;
import org.internship.task.driverservice.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);

    List<Driver> findByIsDeleted(Boolean isDeleted);

    Optional<Driver> findFirstByIsInRideFalseOrderByIdAsc();

}
