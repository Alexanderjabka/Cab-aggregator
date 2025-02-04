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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.internship.task.passengerservice.util.constantMessages.ExceptionMessages.PASSENGER_NOT_FOUND_BY_ID;
import static org.internship.task.passengerservice.util.constantMessages.ExceptionMessages.PASSENGER_IS_DELETED;
import static org.internship.task.passengerservice.util.constantMessages.ExceptionMessages.PASSENGER_ALREADY_EXISTS;
import static org.internship.task.passengerservice.util.constantMessages.ExceptionMessages.PASSENGER_NOT_FOUND_BY_EMAIL;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {


    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PassengerResponse> getAllPassengers() {
        List<Passenger> passengers = passengerRepository.findAll();
        return passengers.stream()
                .map(passenger -> modelMapper.map(passenger, PassengerResponse.class))
                .toList();

    }

    @Override
    public List<PassengerResponse> getAllPassengersByStatus(boolean isDeleted) {
        List<Passenger> passengers = passengerRepository.findByIsDeleted(isDeleted);
        return passengers.stream()
                .map(passenger -> modelMapper.map(passenger, PassengerResponse.class))
                .toList();
    }

    @Override
    public PassengerResponse getPassengerById(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(PASSENGER_NOT_FOUND_BY_ID + id));

        return modelMapper.map(passenger, PassengerResponse.class);
    }

    @Override
    @Transactional
    public PassengerResponse createPassenger(PassengerRequest passengerRequest) {
        String email = passengerRequest.getEmail();
        if (passengerRepository.findByEmail(email).isPresent()) {
            throw new InvalidPassengerOperationException(PASSENGER_ALREADY_EXISTS + email);
        }

        Passenger passenger = modelMapper.map(passengerRequest, Passenger.class);
        passenger.setIsDeleted(false);
        passengerRepository.save(passenger);

        return modelMapper.map(passenger,PassengerResponse.class);
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public PassengerResponse updatePassenger(String email,PassengerRequest passengerRequest) {
        String requestEmail = passengerRequest.getEmail();
        Passenger passenger = passengerRepository.findByEmail(email)
                .orElseThrow(() -> new PassengerNotFoundException(PASSENGER_NOT_FOUND_BY_EMAIL + email));

        if (passenger.getIsDeleted()) {
            throw new InvalidPassengerOperationException(PASSENGER_IS_DELETED + email);
        }

        if (!passenger.getEmail().equals(requestEmail)
                && passengerRepository.findByEmail(requestEmail).isPresent()) {
            throw new InvalidPassengerOperationException(PASSENGER_ALREADY_EXISTS + requestEmail);
        }

        modelMapper.map(passengerRequest,passenger);
        passenger.setIsDeleted(false);
        passengerRepository.save(passenger);

        return modelMapper.map(passenger,PassengerResponse.class);
    }

}
