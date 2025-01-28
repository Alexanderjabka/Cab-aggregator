package org.internship.task.driverservice.services;

import lombok.RequiredArgsConstructor;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.repositories.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    public List<DriverResponse> getAllDrivers() {
    }

    public List<DriverResponse> getAllDriversByStatus(boolean status) {
    }

    public DriverResponse getDriverById(Long id) {
    }

    public DriverResponse createDriver(DriverRequest driverRequest) {
    }

    public void updateDriver(String email, DriverRequest driverRequest) {

    }

    public void deleteDriver(Long id) {

    }
}

