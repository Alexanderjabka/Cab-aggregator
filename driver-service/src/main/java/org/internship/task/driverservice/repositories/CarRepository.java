package org.internship.task.driverservice.repositories;

import org.internship.task.driverservice.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car,Long> {
}
