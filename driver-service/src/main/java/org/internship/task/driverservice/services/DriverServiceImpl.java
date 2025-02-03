package org.internship.task.driverservice.services;

import lombok.RequiredArgsConstructor;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.exceptions.driverException.DriverNotFoundException;
import org.internship.task.driverservice.exceptions.driverException.InvalidDriverOperationException;
import org.internship.task.driverservice.mappers.DriverMapper;
import org.internship.task.driverservice.repositories.DriverRepository;
import org.internship.task.driverservice.services.serviceInterfaces.DriverService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import static org.internship.task.driverservice.util.constantMessages.exceptionMessages.DriverExceptionMessages.DRIVER_ALREADY_EXISTS;
import static org.internship.task.driverservice.util.constantMessages.exceptionMessages.DriverExceptionMessages.DRIVER_NOT_FOUND_BY_EMAIL;
import static org.internship.task.driverservice.util.constantMessages.exceptionMessages.DriverExceptionMessages.DRIVER_IS_DELETED;
import static org.internship.task.driverservice.util.constantMessages.exceptionMessages.DriverExceptionMessages.DRIVER_NOT_FOUND_BY_ID;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    public List<DriverResponse> getAllDrivers() {
        List<Driver> drivers = driverRepository.findAll();
        return DriverMapper.toDtoList(drivers);
    }

    public List<DriverResponse> getAllDriversByStatus(boolean status) {
        List<Driver> drivers = driverRepository.findByIsDeleted(status);
        return DriverMapper.toDtoList(drivers);
    }

    public DriverResponse getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(DRIVER_NOT_FOUND_BY_ID + id));

        return DriverMapper.toDto(driver);
    }

    public DriverResponse createDriver(DriverRequest driverRequest) {
        driverRepository.findByEmail(driverRequest.getEmail())
                .ifPresent(driver -> {
                    throw new InvalidDriverOperationException(DRIVER_ALREADY_EXISTS + driverRequest.getEmail());
                });

        Driver driver = DriverMapper.toEntity(driverRequest);
        driver.setIsDeleted(false);
        driverRepository.save(driver);

        return DriverMapper.toDto(driver);
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

        DriverMapper.toEntity(driverRequest, driver);
        driver.setIsDeleted(false);
        driverRepository.save(driver);

        return DriverMapper.toDto(driver);
    }

    public void deleteDriver(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new DriverNotFoundException(DRIVER_NOT_FOUND_BY_ID + id);
        }

        Optional<Driver> driverOptional = driverRepository.findById(id);
        if (driverOptional.isPresent()) {
            Driver driver = driverOptional.get();
            driver.setIsDeleted(true);

            if (driver.getCars() != null && !driver.getCars().isEmpty()) {
                driver.getCars().forEach(car -> car.setIsDeleted(true));
            }

            driverRepository.save(driver);
        }
    }
}
