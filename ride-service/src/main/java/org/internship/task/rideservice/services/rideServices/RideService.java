package org.internship.task.rideservice.services.rideServices;

import java.util.List;
import org.internship.task.rideservice.dto.RideRequest;
import org.internship.task.rideservice.dto.RideResponse;
import org.internship.task.rideservice.enums.Status;

public interface RideService {
    List<RideResponse> getAllRides();

    List<RideResponse> getAllRidesByStatus(Status status);

    RideResponse getRideById(Long id);

    RideResponse createRide(RideRequest rideRequest);

    RideResponse updateRide(Long id, RideRequest rideRequest);

    RideResponse changeStatus(Long id, Status status);
}
