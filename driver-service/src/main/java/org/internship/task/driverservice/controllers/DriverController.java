package org.internship.task.driverservice.controllers;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.internship.task.driverservice.dto.drivers.DriverListResponse;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
import org.internship.task.driverservice.services.DriverServiceImpl;
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
@RequestMapping("api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverServiceImpl driverService;

    @GetMapping
    public ResponseEntity<DriverListResponse> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<DriverListResponse> getAllDriversByStatus(@PathVariable boolean status) {
        return driverService.getAllDriversByStatus(status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @PostMapping()
    public ResponseEntity<DriverResponse> createDriver(@Valid @RequestBody DriverRequest driverRequest) {
        return ResponseEntity.status(201).body(driverService.createDriver(driverRequest));
    }

    @PutMapping("/{email}")
    public ResponseEntity<DriverResponse> updateDriver(@Valid @PathVariable String email,
                                                       @Valid @RequestBody DriverRequest driverRequest) {
        return ResponseEntity.status(200).body(driverService.updateDriver(email, driverRequest));
    }

    @PutMapping("/assign")
    public ResponseEntity<DriverResponse> assignDriver() {
        return ResponseEntity.status(200).body(driverService.getFirstFreeDriverAndChangeStatus());
    }

    @PutMapping("/release/{driverId}")
    public ResponseEntity<Void> releaseDriver(@PathVariable Long driverId) {
        driverService.releaseDriver(driverId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }
}
