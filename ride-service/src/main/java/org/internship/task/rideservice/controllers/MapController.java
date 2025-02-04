package org.internship.task.rideservice.controllers;

import lombok.RequiredArgsConstructor;
import org.internship.task.rideservice.services.mapServices.MapServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/map")
@RequiredArgsConstructor
public class MapController {
    private final MapServiceImpl mapService;

    @GetMapping("/coordinates")
    public double[] getCoordinates(@RequestParam String address) {
        return mapService.getCoordinates(address);
    }

    @GetMapping("/distance")
    public double getDistance(@RequestParam String start, @RequestParam String finish) {
        return mapService.getDistance(start, finish);
    }
}
