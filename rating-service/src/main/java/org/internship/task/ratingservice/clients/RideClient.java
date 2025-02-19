package org.internship.task.ratingservice.clients;

import org.internship.task.ratingservice.configFeignErrorDecoder.FeignClientConfig;
import org.internship.task.ratingservice.dto.clientsDto.GetRideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ride-service", url = "http://localhost:8082/api/v1/rides", configuration = FeignClientConfig.class)
public interface RideClient {
    @GetMapping("/{id}")
    GetRideResponse getRideById(@PathVariable Long id);
}
