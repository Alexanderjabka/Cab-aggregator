package org.internship.task.driverservice.services;

import static org.internship.task.driverservice.util.constantMessages.exceptionMessages.CarExceptionMessages.CAR_ALREADY_EXISTS;
import static org.internship.task.driverservice.util.constantMessages.exceptionMessages.CarExceptionMessages.CAR_IS_DELETED;
import static org.internship.task.driverservice.util.constantMessages.exceptionMessages.CarExceptionMessages.CAR_NOT_FOUND_BY_CAR_NUMBER;
import static org.internship.task.driverservice.util.constantMessages.exceptionMessages.CarExceptionMessages.CAR_NOT_FOUND_BY_ID;
import static org.internship.task.driverservice.util.constantMessages.exceptionMessages.DriverExceptionMessages.DRIVER_NOT_FOUND_BY_EMAIL;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.internship.task.driverservice.dto.cars.CarListResponse;
import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.cars.CarResponse;
import org.internship.task.driverservice.entities.Car;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.exceptions.carExceptions.CarNotFoundException;
import org.internship.task.driverservice.exceptions.carExceptions.InvalidCarOperationException;
import org.internship.task.driverservice.exceptions.driverException.DriverNotFoundException;
import org.internship.task.driverservice.mappers.CarMapper;
import org.internship.task.driverservice.repositories.CarRepository;
import org.internship.task.driverservice.repositories.DriverRepository;
import org.internship.task.driverservice.services.serviceInterfaces.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final DriverRepository driverRepository;
    private final CarMapper carMapper;

    @Transactiona(readOnly = true)
    public ResponseEntity<CarListResponse> getAllCars() {
        List<Car> cars = carRepository.findAllByOrderByIdAsc();
        return cars.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(CarListResponse.builder()
            .cars(carMapper.toDtoList(cars))
            .build());
    }

    @Transactional(readOnly = true)
    public ResponseEntity<CarListResponse> getAllCarsByStatus(boolean status) {
        List<Car> cars = carRepository.findByIsDeletedOrderByIdAsc(status);
        return cars.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(CarListResponse.builder()
            .cars(carMapper.toDtoList(cars))
            .build());
    }

    @Transactional(readOnly = true)
    public CarResponse getCarById(Long id) {
        Car car = carRepository.findById(id)
            .orElseThrow(() -> new CarNotFoundException(CAR_NOT_FOUND_BY_ID + id));
        return carMapper.toDto(car);
    }

    @Transactional
    public CarResponse createCar(CarRequest carRequest, String driverEmail) {
        carRepository.findByCarNumber(carRequest.getCarNumber())
            .ifPresent(car -> {
                throw new InvalidCarOperationException(CAR_ALREADY_EXISTS + carRequest.getCarNumber());
            });

        Car car = carMapper.toEntity(carRequest);
        car.setIsDeleted(false);

        Driver driver = driverRepository.findByEmail(driverEmail)
            .orElseThrow(() -> new DriverNotFoundException(DRIVER_NOT_FOUND_BY_EMAIL + driverEmail));
        car.setDriver(driver);

        carRepository.save(car);

        return carMapper.toDto(car);
    }

    @Transactional
    public CarResponse updateCar(String carNumber, CarRequest carRequest) {
        Car car = carRepository.findByCarNumber(carNumber)
            .orElseThrow(() -> new CarNotFoundException(CAR_NOT_FOUND_BY_CAR_NUMBER + carNumber));

        if (car.getIsDeleted()) {
            throw new InvalidCarOperationException(CAR_IS_DELETED + carNumber);
        }

        carRepository.findByCarNumber(carRequest.getCarNumber()).ifPresent(existingCar -> {
            if (!existingCar.getId().equals(car.getId())) {
                throw new InvalidCarOperationException(CAR_ALREADY_EXISTS + carRequest.getCarNumber());
            }
        });

        carMapper.updateEntity(car, carRequest);

        car.setIsDeleted(false);
        carRepository.save(car);

        return carMapper.toDto(car);
    }

    @Transactional
    public void deleteCar(Long id) {
        if (!carRepository.existsById(id)) {
            throw new CarNotFoundException(CAR_NOT_FOUND_BY_ID + id);
        }

        Optional<Car> carOptional = carRepository.findById(id);
        if (carOptional.isPresent()) {
            Car car = carOptional.get();
            car.setIsDeleted(true);
            carRepository.save(car);
        }
    }
}
