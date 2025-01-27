package org.internship.task.passengerservice.services;


import lombok.RequiredArgsConstructor;
import org.internship.task.passengerservice.dto.PassengerRequest;
import org.internship.task.passengerservice.dto.PassengerResponse;
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
@RequiredArgsConstructor
public class PassengerService {
    private static final String PASSENGER_NOT_FOUND_BY_ID = "Passenger not found with ID: ";
    private static final String PASSENGER_NOT_FOUND_BY_EMAIL = "Passenger not found with Email: ";
    private static final String PASSENGER_ALREADY_EXISTS = "A Passenger with the same Email already exists: ";
    private static final String PASSENGER_IS_DELETED = "Passenger is deleted and cannot be updated: ";

    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;

    public List<PassengerResponse> getAllPassengers() {

        return passengerRepository.findAll().stream().
                map(passenger -> modelMapper.map(passenger,PassengerResponse.class)).
                toList();
    }

    public List<PassengerResponse> getAllPassengersByStatus(boolean isDeleted) {

        return passengerRepository.findByIsDeleted(isDeleted).stream().
                map(passenger -> modelMapper.map(passenger,PassengerResponse.class)).
                toList();
    }

    public PassengerResponse getPassengerById(Long id) {
        return passengerRepository.findById(id)
                .map(passenger -> modelMapper.map(passenger, PassengerResponse.class))
                .orElseThrow(() -> new PassengerNotFoundException(PASSENGER_NOT_FOUND_BY_ID + id));
    }

    public PassengerResponse getPassengerByEmail(String email) {
        return passengerRepository.findByEmail(email)
                .map(passenger -> modelMapper.map(passenger, PassengerResponse.class))
                .orElseThrow(() -> new PassengerNotFoundException(PASSENGER_NOT_FOUND_BY_EMAIL + email));
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
    public void updatePassenger(String email,PassengerRequest passengerRequest) {
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
    }
}
