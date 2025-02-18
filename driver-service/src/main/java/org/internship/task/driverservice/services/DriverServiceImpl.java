package org.internship.task.driverservice.services;

import static org.internship.task.driverservice.util.constantMessages.exceptionMessages.DriverExceptionMessages.DRIVER_ALREADY_EXISTS;
import static org.internship.task.driverservice.util.constantMessages.exceptionMessages.DriverExceptionMessages.DRIVER_IS_DELETED;
import static org.internship.task.driverservice.util.constantMessages.exceptionMessages.DriverExceptionMessages.DRIVER_NOT_FOUND_BY_EMAIL;
import static org.internship.task.driverservice.util.constantMessages.exceptionMessages.DriverExceptionMessages.DRIVER_NOT_FOUND_BY_ID;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.internship.task.driverservice.dto.drivers.DriverListResponse;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.exceptions.driverException.DriverNotFoundException;
import org.internship.task.driverservice.exceptions.driverException.InvalidDriverOperationException;
import org.internship.task.driverservice.mappers.DriverMapper;
import org.internship.task.driverservice.repositories.DriverRepository;
import org.internship.task.driverservice.services.serviceInterfaces.DriverService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<DriverListResponse> getAllDrivers() {
        List<Driver> drivers = driverRepository.findAllByOrderByIdAsc();
        return drivers.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(DriverListResponse.builder()
            .drivers(driverMapper.toDtoList(drivers))
            .build());
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<DriverListResponse> getAllDriversByStatus(boolean status) {
        List<Driver> drivers = driverRepository.findByIsDeletedOrderByIdAsc(status);
        return drivers.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(DriverListResponse.builder()
            .drivers(driverMapper.toDtoList(drivers))
            .build());
    }

    @Transactional(readOnly = true)
    @Override
    public DriverResponse getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
            .orElseThrow(() -> new DriverNotFoundException(DRIVER_NOT_FOUND_BY_ID + id));

        return driverMapper.toDto(driver);
    }

    @Transactional(readOnly = true)
    @Override
    public DriverResponse getFirstFreeDriverAndChangeStatus() {
        Driver driver = driverRepository.findFirstByIsInRideFalseOrderByIdAsc()
            .orElseThrow(() -> new DriverNotFoundException("Нет свободных водителей"));

        boolean hasCars = driver.getCars().stream()
            .anyMatch(car -> car.getIsDeleted() == Boolean.FALSE);

        if (!hasCars) {
            throw new RuntimeException("У водителя нет машины, чтобы принять заказ");
        }

        driver.setIsInRide(true);
        driverRepository.save(driver);

        return driverMapper.toDto(driver);
    }

    @Transactional
    @Override
    public void releaseDriver(Long driverId) {
        Driver driver = driverRepository.findById(driverId)
            .orElseThrow(() -> new DriverNotFoundException("Водитель с ID " + driverId + " не найден"));

        if (!driver.getIsInRide()) {
            throw new InvalidDriverOperationException("Водитель с ID " + driverId + " уже свободен");
        }

        driver.setIsInRide(false);

        driverRepository.save(driver);
    }


    @Transactional
    @Override
    public DriverResponse createDriver(DriverRequest driverRequest) {
        driverRepository.findByEmail(driverRequest.getEmail())
            .ifPresent(driver -> {
                throw new InvalidDriverOperationException(DRIVER_ALREADY_EXISTS + driverRequest.getEmail());
            });

        Driver driver = driverMapper.toEntity(driverRequest);
        driver.setIsDeleted(false);
        driverRepository.save(driver);

        return driverMapper.toDto(driver);
    }

    @Transactional
    @Override
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

        driverMapper.updateEntity(driver, driverRequest);
        driverRepository.save(driver);

        return driverMapper.toDto(driver);
    }

    @Transactional
    @Override
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
