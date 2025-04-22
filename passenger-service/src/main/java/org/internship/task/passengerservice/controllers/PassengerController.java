package org.internship.task.passengerservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.internship.task.passengerservice.dto.PassengerListResponse;
import org.internship.task.passengerservice.dto.PassengerRequest;
import org.internship.task.passengerservice.dto.PassengerResponse;
import org.internship.task.passengerservice.services.PassengerServiceImpl;
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
@RequestMapping("api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerServiceImpl passengerService;

    @GetMapping
    public ResponseEntity<PassengerListResponse> getAllPassengers() {
        return passengerService.getAllPassengers();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<PassengerListResponse> getAllPassengersByStatus(@PathVariable boolean status) {
        return passengerService.getAllPassengersByStatus(status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getPassengerById(id));
    }

    @GetMapping("/isFree/{id}")
    public ResponseEntity<PassengerResponse> getPassengerByIdAndStatus(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getPassengerByIdAndStatus(id));
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(@Valid @RequestBody PassengerRequest passengerRequest) {
        return ResponseEntity.status(201).body(passengerService.createPassenger(passengerRequest));
    }

    @PutMapping("/{email}")
    public ResponseEntity<PassengerResponse> updatePassenger(
        @Valid @PathVariable String email,
        @Valid @RequestBody PassengerRequest passengerRequest) {
        return ResponseEntity.ok(passengerService.updatePassenger(email, passengerRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }

}
