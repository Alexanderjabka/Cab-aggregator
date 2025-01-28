package org.internship.task.driverservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
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
    public ResponseEntity<List<DriverResponse>> getAllCars(){
        return ResponseEntity.ok(carService.getAllCars());
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DriverResponse>> getAllCarsByStatus(@PathVariable boolean status){
        return ResponseEntity.ok(carService.getAllCarsByStatus(status));
    }
    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getCarById(@PathVariable Long id){
        return ResponseEntity.ok(carService.getCarById(id));
    }
    @PostMapping("/{driverEmail}")
    public ResponseEntity<DriverResponse> createCar(@Valid @RequestBody CarRequest carRequest,@PathVariable String driverEmail ) {
        return ResponseEntity.status(201).body(carService.createCar(carRequest,driverEmail));
    }
    @PutMapping("/{carNumber}")
    public ResponseEntity<String> updateCar(@Valid @PathVariable String carNumber,@Valid @RequestBody CarRequest carRequest) {
        carService.updateCar(carNumber, carRequest);
        return ResponseEntity.ok("Passenger updated successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
