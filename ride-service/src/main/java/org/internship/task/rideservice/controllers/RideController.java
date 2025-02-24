package org.internship.task.rideservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.internship.task.rideservice.dto.RideListResponse;
import org.internship.task.rideservice.dto.RideRequest;
import org.internship.task.rideservice.dto.RideResponse;
import org.internship.task.rideservice.dto.StatusRequest;
import org.internship.task.rideservice.enums.Status;
import org.internship.task.rideservice.services.rideServices.RideServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideServiceImpl rideService;

    @GetMapping
    public ResponseEntity<RideListResponse> getAllRides() {
        return rideService.getAllRides();
    }

    @GetMapping("/status")
    public ResponseEntity<RideListResponse> getAllRidesByStatus(@RequestParam Status status) {
        return rideService.getAllRidesByStatus(status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getRideById(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.getRideById(id));
    }

    @GetMapping("/canBeRate/{id}")
    public ResponseEntity<RideResponse> getRideByIdAndAbilityToRate(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.getRideByIdAndAbilityToRate(id));
    }

    @PostMapping()
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody RideRequest rideRequest) {
        return ResponseEntity.status(201).body(rideService.createRide(rideRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RideResponse> updateRide(@Valid @PathVariable Long id,
                                                   @Valid @RequestBody RideRequest rideRequest) {
        return ResponseEntity.status(200).body(rideService.updateRide(id, rideRequest));
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<RideResponse> changeStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest status) {
        return ResponseEntity.status(200).body(rideService.changeStatus(id, status));
    }

}
