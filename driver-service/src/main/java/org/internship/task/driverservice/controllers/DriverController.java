package org.internship.task.driverservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
import org.internship.task.driverservice.services.DriverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @GetMapping()
    public ResponseEntity<List<DriverResponse>> getAllDrivers(){
        return ResponseEntity.ok(driverService.getAllDrivers());
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DriverResponse>> getAllDriversByStatus(@PathVariable boolean status){
        return ResponseEntity.ok(driverService.getAllDriversByStatus(status));
    }
    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id){
        return ResponseEntity.ok(driverService.getDriverById(id));
    }
    @PostMapping()
    public ResponseEntity<DriverResponse> createDriver(@Valid @RequestBody DriverRequest driverRequest) {
        return ResponseEntity.status(201).body(driverService.createDriver(driverRequest));
    }
    @PutMapping("/{email}")
    public ResponseEntity<String> updateDriver(@Valid @PathVariable String email,@Valid @RequestBody DriverRequest driverRequest) {
        driverService.updateDriver(email, driverRequest);
        return ResponseEntity.ok("Passenger updated successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
            driverService.deleteDriver(id);
            return ResponseEntity.noContent().build();
    }
}
