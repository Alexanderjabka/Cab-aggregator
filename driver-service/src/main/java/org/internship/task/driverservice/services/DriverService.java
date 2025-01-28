package org.internship.task.driverservice.services;

import lombok.RequiredArgsConstructor;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.exceptions.driverException.DriverNotFoundException;
import org.internship.task.driverservice.exceptions.driverException.InvalidDriverOperationException;
import org.internship.task.driverservice.repositories.DriverRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final ModelMapper modelMapper;

    public List<DriverResponse> getAllDrivers() {
        List<Driver> drivers = driverRepository.findAll();
        return drivers.stream()
                .map(driver -> modelMapper.map(driver, DriverResponse.class))
                .collect(Collectors.toList());
    }

    public List<DriverResponse> getAllDriversByStatus(boolean status) {
        List<Driver> drivers = driverRepository.findByIsDeleted(status);
        return drivers.stream()
                .map(driver -> modelMapper.map(driver, DriverResponse.class))
                .collect(Collectors.toList());
    }

    public DriverResponse getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found by ID: " + id));

        return modelMapper.map(driver, DriverResponse.class);
    }

    public DriverResponse createDriver(DriverRequest driverRequest) {
        if (driverRepository.findByEmail(driverRequest.getEmail()).isPresent()) {
            throw new InvalidDriverOperationException(DRIVER_ALREADY_EXISTS + driverRequest.getEmail());
        }

        Driver driver = modelMapper.map(driverRequest, Driver.class);
        driver.setIsDeleted(false);
        driverRepository.save(driver);

        return modelMapper.map(driver,DriverResponse.class);
    }

    public DriverResponse updateDriver(String email, DriverRequest driverRequest) {
        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow(() -> new DriverNotFoundException(DRIVER_NOT_FOUND_BY_EMAIL + email));

        if (driver.getIsDeleted()) {
            throw new InvalidDriverOperationException(DRIVER_IS_DELETED + email);
        }

        driverRepository.findByEmail(driverRequest.getEmail()).ifPresent(existingDriver -> {
            if (!existingDriver.getId().equals(driver.getId())) {
                throw new InvalidDriverOperationException(DRIVER_ALREADY_EXISTS + driverRequest.getEmail());
            }
        });

        modelMapper.map(driverRequest,driver);
        driver.setIsDeleted(false);
        driverRepository.save(driver);

        return modelMapper.map(driver,DriverResponse.class);

    }

    public void deleteDriver(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new DriverNotFoundException(DRIVER_NOT_FOUND_BY_ID + id);
        }

        Optional<Driver> driverOptional = driverRepository.findById(id);
        if (driverOptional.isPresent()) {
            Driver driver = driverOptional.get();
            driver.setIsDeleted(true);
            driverRepository.save(driver);
        }
    }

}

