package org.internship.task.rideservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.internship.task.rideservice.dto.RideRequest;
import org.internship.task.rideservice.dto.RideResponse;
import org.internship.task.rideservice.enums.Status;
import org.internship.task.rideservice.mapService.MapService;
import org.internship.task.rideservice.services.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/rides")
@RequiredArgsConstructor
public class RideController {
    private final RideService rideService;
    private final MapService mapService;
    @GetMapping()
    public ResponseEntity<List<RideResponse>> getAllRides(){
        return ResponseEntity.ok(rideService.getAllRides());
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RideResponse>> getAllRidesByStatus(@PathVariable Status status){
        return ResponseEntity.ok(rideService.getAllRidesByStatus(status));
    }
    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getRideById(@PathVariable Long id){
        return ResponseEntity.ok(rideService.getRideById(id));
    }
    @PostMapping()
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody RideRequest rideRequest) {
        return ResponseEntity.status(201).body(rideService.createRide(rideRequest));
    }
    @PutMapping("/{id}")
    public ResponseEntity<RideResponse> updateRide(@Valid @PathVariable Long id, @Valid @RequestBody RideRequest rideRequest) {
        return ResponseEntity.status(200).body(rideService.updateRide(id, rideRequest));
    }
    @PutMapping("/change-status/{id}")
    public ResponseEntity<RideResponse> changeStatus(@PathVariable Long id,@Valid @RequestBody Status status) {
        return ResponseEntity.status(200).body(rideService.changeStatus(id,status));
    }
    @GetMapping("/coordinates")
    public double[] getCoordinates(@RequestParam String address) {
        return mapService.getCoordinates(address);
    }

    @GetMapping("/distance")
    public double getDistance(@RequestParam String start, @RequestParam String finish) {
        return mapService.getDistance(start, finish);
    }
}
