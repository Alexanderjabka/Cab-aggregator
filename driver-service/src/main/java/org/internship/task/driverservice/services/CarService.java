package org.internship.task.driverservice.services;

import lombok.RequiredArgsConstructor;
import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.cars.CarResponse;
import org.internship.task.driverservice.entities.Car;
import org.internship.task.driverservice.exceptions.driverException.DriverNotFoundException;
import org.internship.task.driverservice.exceptions.driverException.InvalidDriverOperationException;
import org.internship.task.driverservice.repositories.CarRepository;
import org.internship.task.driverservice.repositories.DriverRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final DriverRepository driverRepository;
    private final ModelMapper modelMapper;

    public List<CarResponse> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .map(car -> modelMapper.map(car, CarResponse.class))
                .collect(Collectors.toList());

    }

    public List<CarResponse> getAllCarsByStatus(boolean status) {
        List<Car> cars = carRepository.findByIsDeleted(status);
        return cars.stream()
                .map(car -> modelMapper.map(car, CarResponse.class))
                .collect(Collectors.toList());
    }

    public CarResponse getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car not found by ID: " + id));

        return modelMapper.map(car, CarResponse.class);
    }

    public CarResponse createCar(CarRequest carRequest, String driverEmail) {
        if (carRepository.findByCarNumber(carRequest.getCarNumber()).isPresent()) {
            throw new InvalidCarOperationException(CAR_ALREADY_EXISTS + carRequest.getCarNumber());
        }

        Car car = modelMapper.map(carRequest, Car.class);
        car.setIsDeleted(false);
        car.setDriver(driverRepository.findByEmail(driverEmail).orElseThrow(DRIVER_NOT_FOUND_BY_EMAIL + driverEmail));
        carRepository.save(car);

        return modelMapper.map(car,CarResponse.class);
    }

    public CarResponse updateCar(String carNumber, CarRequest carRequest) {
        Car car = carRepository.findByCarNumber(carNumber)
                .orElseThrow(() -> new DriverNotFoundException(CAR_NOT_FOUND_BY_CAR_NUMBER + carNumber));

        if (car.getIsDeleted()) {
            throw new InvalidCarOperationException(CAR_IS_DELETED + carNumber);
        }

        carRepository.findByCarNumber(carRequest.getCarNumber()).ifPresent(existingCar -> {
            if (!existingCar.getId().equals(car.getId())) {
                throw new InvalidDriverOperationException(CAR_ALREADY_EXISTS + carRequest.getCarNumber());
            }
        });

        modelMapper.map(carRequest,car);
        car.setIsDeleted(false);
        carRepository.save(car);

        return modelMapper.map(car, CarResponse.class);
    }

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
