package org.internship.task.rideservice.services.rideServices;

import org.internship.task.rideservice.dto.RideRequest;
import org.internship.task.rideservice.dto.RideResponse;
import org.internship.task.rideservice.enums.Status;

import java.util.List;

public interface RideService {
    public List<RideResponse> getAllRides();
    public List<RideResponse> getAllRidesByStatus(Status status);
    public RideResponse getRideById(Long id);
    public RideResponse createRide(RideRequest rideRequest);
    public RideResponse updateRide(Long id, RideRequest rideRequest);
    public RideResponse changeStatus(Long id,Status status);

}
