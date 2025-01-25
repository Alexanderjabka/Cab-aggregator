package org.internship.task.passengerservice.controllers;


import org.internship.task.passengerservice.DTO.PassengerRequest;
import org.internship.task.passengerservice.DTO.PassengerResponse;
import org.internship.task.passengerservice.repositories.PassengerRepository;
import org.internship.task.passengerservice.services.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerService passengerService;
    private final PassengerRepository passengerRepository;

    @Autowired
    public PassengerController(PassengerService passengerService, PassengerRepository passengerRepository) {
        this.passengerService = passengerService;
        this.passengerRepository = passengerRepository;
    }


    @GetMapping()
    public ResponseEntity<List<PassengerResponse>> getAllPassengers(){
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<PassengerResponse>> getPassengerById(@PathVariable Long id){
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> addPassenger(@RequestBody PassengerRequest passengerRequest) {
        passengerService.createPassenger(passengerRequest);

        return ResponseEntity.status(201).body(passengerService.getPassengerByEmail(passengerRequest.getEmail()));
    }
    @PutMapping("/{email}")
    public ResponseEntity<String> updatePassenger(@PathVariable String email, @RequestBody PassengerRequest passengerRequest) {
        passengerService.updatePassenger(email, passengerRequest);
        return ResponseEntity.ok("Passenger updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePassanger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        //204
        return ResponseEntity.noContent().build();
    }


}
