package org.internship.task.passengerservice.services;

import static org.internship.task.passengerservice.util.constantMessages.ExceptionMessages.PASSENGER_ALREADY_EXISTS;
import static org.internship.task.passengerservice.util.constantMessages.ExceptionMessages.PASSENGER_IS_DELETED;
import static org.internship.task.passengerservice.util.constantMessages.ExceptionMessages.PASSENGER_NOT_FOUND_BY_EMAIL;
import static org.internship.task.passengerservice.util.constantMessages.ExceptionMessages.PASSENGER_NOT_FOUND_BY_ID;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.internship.task.passengerservice.dto.PassengerListResponse;
import org.internship.task.passengerservice.dto.PassengerRequest;
import org.internship.task.passengerservice.dto.PassengerResponse;
import org.internship.task.passengerservice.entities.Passenger;
import org.internship.task.passengerservice.exceptions.passengerExceptions.InvalidPassengerOperationException;
import org.internship.task.passengerservice.exceptions.passengerExceptions.PassengerNotFoundException;
import org.internship.task.passengerservice.mappers.PassengerMapper;
import org.internship.task.passengerservice.repositories.PassengerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<PassengerListResponse> getAllPassengers() {
        List<Passenger> passengers = passengerRepository.findAllByOrderByIdAsc();
        return passengers.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(PassengerListResponse.builder()
            .passengers(passengerMapper.toDtoList(passengers))
            .build());
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<PassengerListResponse> getAllPassengersByStatus(boolean isDeleted) {
        List<Passenger> passengers = passengerRepository.findByIsDeletedOrderByIdAsc(isDeleted);
        return passengers.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(PassengerListResponse.builder()
            .passengers(passengerMapper.toDtoList(passengers))
            .build());
    }

    @Transactional(readOnly = true)
    @Override
    public PassengerResponse getPassengerById(Long id) {
        Passenger passenger = passengerRepository.findById(id)
            .orElseThrow(() -> new PassengerNotFoundException(PASSENGER_NOT_FOUND_BY_ID + id));
        return passengerMapper.toDto(passenger);
    }

    @Override
    @Transactional
    public PassengerResponse createPassenger(PassengerRequest passengerRequest) {
        String email = passengerRequest.getEmail();
        if (passengerRepository.findByEmail(email).isPresent()) {
            throw new InvalidPassengerOperationException(PASSENGER_ALREADY_EXISTS + email);
        }

        Passenger passenger = passengerMapper.toEntity(passengerRequest);
        passenger.setIsDeleted(false);
        passengerRepository.save(passenger);

        return passengerMapper.toDto(passenger);
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
    public PassengerResponse updatePassenger(String email, PassengerRequest passengerRequest) {
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

        passengerMapper.updateEntity(passenger, passengerRequest);
        passengerRepository.save(passenger);

        return passengerMapper.toDto(passenger);
    }
}
