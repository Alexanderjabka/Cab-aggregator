package org.internship.task.passengerservice.services;


import jakarta.validation.Valid;
import org.internship.task.passengerservice.DTO.PassengerRequest;
import org.internship.task.passengerservice.DTO.PassengerResponse;
import org.internship.task.passengerservice.entities.Passenger;
import org.internship.task.passengerservice.exceptions.InvalidPassengerOperationException;
import org.internship.task.passengerservice.exceptions.PassengerNotFoundException;
import org.internship.task.passengerservice.repositories.PassengerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {
    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PassengerService(PassengerRepository passengerRepository, ModelMapper modelMapper) {
        this.passengerRepository = passengerRepository;
        this.modelMapper = modelMapper;
    }

    public List<PassengerResponse> getAllPassengers() {

        return passengerRepository.findAll().stream().
                map(passenger -> modelMapper.map(passenger,PassengerResponse.class)).
                toList();
    }

    public PassengerResponse getPassengerById(Long id) {
        return passengerRepository.findById(id)
                .map(passenger -> modelMapper.map(passenger, PassengerResponse.class))
                .orElseThrow(() -> new PassengerNotFoundException("Passenger not found with ID: " + id));
    }
    public PassengerResponse getPassengerByEmail(String email) {
        return passengerRepository.findByEmail(email)
                .map(passenger -> modelMapper.map(passenger, PassengerResponse.class))
                .orElseThrow(() -> new PassengerNotFoundException("Passenger not found with Email: " + email));
    }
    public void createPassenger(PassengerRequest passengerRequest) {

        if (passengerRepository.findByEmail(passengerRequest.getEmail()).isPresent()) {
            throw new InvalidPassengerOperationException("A Passenger with the same Email already exists: " + passengerRequest.getEmail());
        }

        Passenger passenger = modelMapper.map(passengerRequest, Passenger.class);
        passenger.setIsDeleted(false);
        passengerRepository.save(passenger);

    }

    public void deletePassenger(Long id) {
        if (!passengerRepository.existsById(id)) {
            throw new PassengerNotFoundException("Passenger not found with ID: " + id);
        }

        Optional<Passenger> passengerOptional = passengerRepository.findById(id);
        if (passengerOptional.isPresent()) {
            Passenger passenger = passengerOptional.get();
            passenger.setIsDeleted(true);
            passengerRepository.save(passenger);
        }
    }
    public void updatePassenger(String email,PassengerRequest passengerRequest) {
        Passenger passenger = passengerRepository.findByEmail(email)
                .orElseThrow(() -> new PassengerNotFoundException("Passenger not found with Email: " + email));

        if (passenger.getIsDeleted()) {
            throw new InvalidPassengerOperationException("Passenger is deleted and cannot be updated: " + email);
        }

        passengerRepository.findByEmail(passengerRequest.getEmail()).ifPresent(existingPassenger -> {
            if (!existingPassenger.getId().equals(passenger.getId())) {
                throw new InvalidPassengerOperationException("A Passenger with the same Email already exists: " + passengerRequest.getEmail());
            }
        });

        passenger.setEmail(passengerRequest.getEmail());
        passenger.setName(passengerRequest.getName());
        passenger.setPhoneNumber(passengerRequest.getPhoneNumber());
        passenger.setIsDeleted(false);
        passengerRepository.save(passenger);
    }


}
