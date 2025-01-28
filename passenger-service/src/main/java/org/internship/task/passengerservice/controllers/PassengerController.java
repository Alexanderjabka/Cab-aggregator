package org.internship.task.passengerservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.internship.task.passengerservice.dto.PassengerRequest;
import org.internship.task.passengerservice.dto.PassengerResponse;
import org.internship.task.passengerservice.services.PassengerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping()
    public ResponseEntity<List<PassengerResponse>> getAllPassengers(){
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PassengerResponse>> getAllPassengersByStatus(@PathVariable boolean status){
        return ResponseEntity.ok(passengerService.getAllPassengersByStatus(status));
    }
    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id){
        return ResponseEntity.ok(passengerService.getPassengerById(id));
    }
    @PostMapping()
    public ResponseEntity<PassengerResponse> createPassenger(@Valid @RequestBody PassengerRequest passengerRequest) {
        return ResponseEntity.status(201).body(passengerService.createPassenger(passengerRequest));
    }
    @PutMapping("/{email}")
    public ResponseEntity<PassengerResponse> updatePassenger(@Valid @PathVariable String email,@Valid @RequestBody PassengerRequest passengerRequest) {
        return ResponseEntity.status(200).body(passengerService.updatePassenger(email,passengerRequest));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }
}
