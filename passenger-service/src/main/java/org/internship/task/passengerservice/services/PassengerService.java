package org.internship.task.passengerservice.services;

import org.internship.task.passengerservice.dto.PassengerRequest;
import org.internship.task.passengerservice.dto.PassengerResponse;

import java.util.List;

public interface PassengerService {
    List<PassengerResponse> getAllPassengers();
    List<PassengerResponse> getAllPassengersByStatus(boolean isDeleted);
    PassengerResponse getPassengerById(Long id);
    PassengerResponse createPassenger(PassengerRequest passengerRequest);
    void deletePassenger(Long id);
    PassengerResponse updatePassenger(String email, PassengerRequest passengerRequest);
}
