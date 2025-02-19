package org.internship.task.rideservice.clients;

import org.internship.task.rideservice.configFeignErrorDecoder.FeignClientConfig;
import org.internship.task.rideservice.dto.clientsdDto.GeocodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "map-service", url = "${server.url}", configuration = FeignClientConfig.class)
public interface MapClient {
    @GetMapping("/search")
    GeocodeResponse getCoordinates(@RequestParam("api_key") String apiKey, @RequestParam("text") String address);
}

