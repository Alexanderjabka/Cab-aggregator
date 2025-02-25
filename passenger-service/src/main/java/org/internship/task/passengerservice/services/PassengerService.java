package org.internship.task.passengerservice.services;

import org.internship.task.passengerservice.dto.PassengerListResponse;
import org.internship.task.passengerservice.dto.PassengerRequest;
import org.internship.task.passengerservice.dto.PassengerResponse;
import org.springframework.http.ResponseEntity;

public interface PassengerService {
    ResponseEntity<PassengerListResponse> getAllPassengers();

    ResponseEntity<PassengerListResponse> getAllPassengersByStatus(boolean isDeleted);

    PassengerResponse getPassengerById(Long id);

    PassengerResponse getPassengerByIdAndStatus(Long id);

    PassengerResponse createPassenger(PassengerRequest passengerRequest);

    void deletePassenger(Long id);

    PassengerResponse updatePassenger(String email, PassengerRequest passengerRequest);
}
