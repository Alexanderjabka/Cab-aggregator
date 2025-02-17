package org.internship.task.rideservice.clients;

import org.internship.task.rideservice.dto.clientsdDto.GetPassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "passenger-service", url = "http://localhost:8080/api/v1/passengers")
public interface PassengerClient {
    @GetMapping("/{id}")
    GetPassengerResponse getPassengerById(@PathVariable Long id);
}
