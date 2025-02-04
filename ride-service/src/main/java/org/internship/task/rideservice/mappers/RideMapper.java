package org.internship.task.rideservice.mappers;

import org.internship.task.rideservice.dto.RideRequest;
import org.internship.task.rideservice.dto.RideResponse;
import org.internship.task.rideservice.entities.Ride;

import java.util.List;
import java.util.stream.Collectors;

public class RideMapper {
    public static Ride toEntity(RideRequest rideRequest) {
        Ride ride = new Ride();

        ride.setPassengerId(rideRequest.getPassengerId());
        ride.setDriverId(rideRequest.getDriverId());
        ride.setStartAddress(rideRequest.getStartAddress());
        ride.setFinishAddress(rideRequest.getFinishAddress());

        return ride;
    }

    public static RideResponse toDto(Ride ride) {
        RideResponse rideResponse = new RideResponse();

        rideResponse.setId(ride.getId());
        rideResponse.setPassengerId(ride.getPassengerId());
        rideResponse.setDriverId(ride.getDriverId());
        rideResponse.setStartAddress(ride.getStartAddress());
        rideResponse.setFinishAddress(ride.getFinishAddress());
        rideResponse.setPrice(ride.getPrice());
        rideResponse.setStatus(ride.getStatus());
        rideResponse.setOrderDateTime(ride.getOrderDateTime());

        return rideResponse;
    }

    public static List<RideResponse> toDtoList(List<Ride> rides) {
        return rides.stream().map(RideMapper::toDto).collect(Collectors.toList());
    }

    public static void toEntity(RideRequest rideRequest, Ride ride) {
        ride.setDriverId(rideRequest.getDriverId());
        ride.setFinishAddress(rideRequest.getFinishAddress());
        ride.setPassengerId(rideRequest.getPassengerId());
        ride.setStartAddress(rideRequest.getStartAddress());
    }
}
