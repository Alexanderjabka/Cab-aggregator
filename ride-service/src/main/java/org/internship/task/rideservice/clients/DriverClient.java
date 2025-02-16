package org.internship.task.rideservice.clients;

import org.internship.task.rideservice.dto.AssignDriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "driver-service", url = "http://localhost:8081/api/v1/drivers")
public interface DriverClient {

    @PutMapping("/assign")
    AssignDriverResponse assignDriver();

    @PutMapping("/release/{driverId}")
    void releaseDriver(@PathVariable Long driverId);
}
