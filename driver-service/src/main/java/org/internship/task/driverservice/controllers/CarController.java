package org.internship.task.driverservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.internship.task.driverservice.dto.cars.CarListResponse;
import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.cars.CarResponse;
import org.internship.task.driverservice.services.CarServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarServiceImpl carService;

    @GetMapping
    public ResponseEntity<CarListResponse> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<CarListResponse> getAllCarsByStatus(@PathVariable boolean status) {
        return carService.getAllCarsByStatus(status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @PostMapping("/{driverEmail}")
    public ResponseEntity<CarResponse> createCar(@Valid @RequestBody CarRequest carRequest,
                                                 @PathVariable String driverEmail) {
        return ResponseEntity.status(201).body(carService.createCar(carRequest, driverEmail));
    }

    @PutMapping("/{carNumber}")
    public ResponseEntity<CarResponse> updateCar(@Valid @PathVariable String carNumber,
                                                 @Valid @RequestBody CarRequest carRequest) {
        return ResponseEntity.status(200).body(carService.updateCar(carNumber, carRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
