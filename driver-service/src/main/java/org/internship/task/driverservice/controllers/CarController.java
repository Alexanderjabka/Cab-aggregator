package org.internship.task.driverservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.cars.CarResponse;
import org.internship.task.driverservice.services.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    @GetMapping()
    public ResponseEntity<List<CarResponse>> getAllCars(){
        return ResponseEntity.ok(carService.getAllCars());
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CarResponse>> getAllCarsByStatus(@PathVariable boolean status){
        return ResponseEntity.ok(carService.getAllCarsByStatus(status));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id){
        return ResponseEntity.ok(carService.getCarById(id));
    }
    @PostMapping("/{driverEmail}")
    public ResponseEntity<CarResponse> createCar(@Valid @RequestBody CarRequest carRequest,@PathVariable String driverEmail ) {
        return ResponseEntity.status(201).body(carService.createCar(carRequest,driverEmail));
    }
    @PutMapping("/{carNumber}")
    public ResponseEntity<CarResponse> updateCar(@Valid @PathVariable String carNumber, @Valid @RequestBody CarRequest carRequest) {
        return ResponseEntity.status(200).body(carService.updateCar(carNumber, carRequest));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
