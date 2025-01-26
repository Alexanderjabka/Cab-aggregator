package org.internship.task.passengerservice.controllers;




import jakarta.validation.Valid;
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

    @Autowired
    public PassengerController(PassengerService passengerService, PassengerRepository passengerRepository) {
        this.passengerService = passengerService;
    }


    @GetMapping()
    public ResponseEntity<List<PassengerResponse>> getAllPassengers(){
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id){
        return ResponseEntity.ok(passengerService.getPassengerById(id));
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(@Valid @RequestBody PassengerRequest passengerRequest) {
        passengerService.createPassenger(passengerRequest);

        return ResponseEntity.status(201).body(passengerService.getPassengerByEmail(passengerRequest.getEmail()));
    }
    @PutMapping("/{email}")
    public ResponseEntity<String> updatePassenger(@Valid @PathVariable String email,@Valid @RequestBody PassengerRequest passengerRequest) {
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
