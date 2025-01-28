package org.internship.task.passengerservice.services;

import lombok.RequiredArgsConstructor;
import org.internship.task.passengerservice.dto.PassengerRequest;
import org.internship.task.passengerservice.dto.PassengerResponse;
import org.internship.task.passengerservice.entities.Passenger;
import org.internship.task.passengerservice.exceptions.InvalidPassengerOperationException;
import org.internship.task.passengerservice.exceptions.PassengerNotFoundException;
import org.internship.task.passengerservice.repositories.PassengerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.internship.task.passengerservice.util.constantMessages.ExceptionMessages.PASSENGER_NOT_FOUND_BY_ID;
import static org.internship.task.passengerservice.util.constantMessages.ExceptionMessages.PASSENGER_IS_DELETED;
import static org.internship.task.passengerservice.util.constantMessages.ExceptionMessages.PASSENGER_ALREADY_EXISTS;
import static org.internship.task.passengerservice.util.constantMessages.ExceptionMessages.PASSENGER_NOT_FOUND_BY_EMAIL;

@Service
@RequiredArgsConstructor
public class PassengerService {


    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;

    public List<PassengerResponse> getAllPassengers() {
        List<Passenger> passengers = passengerRepository.findAll();
        return passengers.stream()
                .map(passenger -> modelMapper.map(passenger, PassengerResponse.class))
                .collect(Collectors.toList());

    }

    public List<PassengerResponse> getAllPassengersByStatus(boolean isDeleted) {
        List<Passenger> passengers = passengerRepository.findByIsDeleted(isDeleted);
        return passengers.stream()
                .map(passenger -> modelMapper.map(passenger, PassengerResponse.class))
                .collect(Collectors.toList());
    }

    public PassengerResponse getPassengerById(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException("Passenger not found by ID: " + id));

        return modelMapper.map(passenger, PassengerResponse.class);
    }

    public PassengerResponse createPassenger(PassengerRequest passengerRequest) {

        if (passengerRepository.findByEmail(passengerRequest.getEmail()).isPresent()) {
            throw new InvalidPassengerOperationException(PASSENGER_ALREADY_EXISTS + passengerRequest.getEmail());
        }

        Passenger passenger = modelMapper.map(passengerRequest, Passenger.class);
        passenger.setIsDeleted(false);
        passengerRepository.save(passenger);

        return modelMapper.map(passenger,PassengerResponse.class);
    }

    public void deletePassenger(Long id) {
        if (!passengerRepository.existsById(id)) {
            throw new PassengerNotFoundException(PASSENGER_NOT_FOUND_BY_ID + id);
        }

        Optional<Passenger> passengerOptional = passengerRepository.findById(id);
        if (passengerOptional.isPresent()) {
            Passenger passenger = passengerOptional.get();
            passenger.setIsDeleted(true);
            passengerRepository.save(passenger);
        }
    }
    public PassengerResponse updatePassenger(String email,PassengerRequest passengerRequest) {
        Passenger passenger = passengerRepository.findByEmail(email)
                .orElseThrow(() -> new PassengerNotFoundException(PASSENGER_NOT_FOUND_BY_EMAIL + email));

        if (passenger.getIsDeleted()) {
            throw new InvalidPassengerOperationException(PASSENGER_IS_DELETED + email);
        }

        passengerRepository.findByEmail(passengerRequest.getEmail()).ifPresent(existingPassenger -> {
            if (!existingPassenger.getId().equals(passenger.getId())) {
                throw new InvalidPassengerOperationException(PASSENGER_ALREADY_EXISTS + passengerRequest.getEmail());
            }
        });

        modelMapper.map(passengerRequest,passenger);
        passenger.setIsDeleted(false);
        passengerRepository.save(passenger);

        return modelMapper.map(passenger,PassengerResponse.class);
    }

}
