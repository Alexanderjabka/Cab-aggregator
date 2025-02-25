package org.internship.task.rideservice.clients;

import org.internship.task.rideservice.configFeignErrorDecoder.FeignClientConfig;
import org.internship.task.rideservice.dto.clientsdDto.GetPassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "passenger-service", url = "${server.url.passenger}", configuration = FeignClientConfig.class)
public interface PassengerClient {
    @GetMapping("/isFree/{id}")
    GetPassengerResponse getPassengerByIdAndStatus(@PathVariable Long id);
}
