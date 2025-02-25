package org.internship.task.ratingservice.controllers;

import lombok.RequiredArgsConstructor;
import org.internship.task.ratingservice.dto.RatingListResponse;
import org.internship.task.ratingservice.dto.RatingRequest;
import org.internship.task.ratingservice.dto.RatingResponse;
import org.internship.task.ratingservice.services.RatingServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingServiceImpl ratingService;

    @GetMapping
    public ResponseEntity<RatingListResponse> getAllRatings() {
        return ratingService.getAllRatings();
    }

    @PostMapping
    public ResponseEntity<RatingResponse> createRating(@RequestBody RatingRequest ratingRequest) {
        return ResponseEntity.status(200).body(ratingService.createRating(ratingRequest));
    }

    @GetMapping("/average/passenger-rating/{passengerId}")
    public ResponseEntity<Double> getAveragePassengerRating(@PathVariable Long passengerId) {
        return ResponseEntity.ok(ratingService.getAveragePassengerRating(passengerId));
    }

    @GetMapping("/average/driver-rating/{driverId}")
    public ResponseEntity<Double> getAverageDriverRating(@PathVariable Long driverId) {
        return ResponseEntity.ok(ratingService.getAverageDriverRating(driverId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRating(@PathVariable Long id) {
        return ratingService.deleteRating(id);
    }

}
