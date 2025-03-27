package org.internship.task.ratingservice.clients;

import org.internship.task.ratingservice.configFeignErrorDecoder.FeignClientConfig;
import org.internship.task.ratingservice.dto.clientsDto.GetRideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ride-service", path = "/api/v1/rides", configuration = FeignClientConfig.class)
public interface RideClient {
    @GetMapping("/canBeRate/{id}")
    GetRideResponse getRideByIdAndAbilityToRate(@PathVariable Long id);
}
