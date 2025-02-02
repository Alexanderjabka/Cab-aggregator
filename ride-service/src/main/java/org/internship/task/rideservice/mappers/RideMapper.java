package org.internship.task.rideservice.mappers;

import org.internship.task.rideservice.dto.RideRequest;
import org.internship.task.rideservice.dto.RideResponse;
import org.internship.task.rideservice.entities.Ride;

import java.util.List;
import java.util.stream.Collectors;

public class RideMapper {
    public static Ride toEntity(RideRequest rideRequest) {
        Ride ride = new Ride();

        ride.setDriverId(rideRequest.getDriverId());
        ride.setPrice(rideRequest.getPrice());
//        ride.setStatus(rideRequest.getStatus());
        ride.setFinishAddress(rideRequest.getFinishAddress());
        ride.setPassengerId(rideRequest.getPassengerId());
        ride.setStartAddress(rideRequest.getStartAddress());
        ride.setOrderDateTime(rideRequest.getOrderDateTime());
        return ride;
    }

    public static RideResponse toDto(Ride ride) {
        RideResponse rideResponse = new RideResponse();

        rideResponse.setId(ride.getId());
        rideResponse.setDriverId(ride.getDriverId());
        rideResponse.setPrice(ride.getPrice());
        rideResponse.setStatus(ride.getStatus());
        rideResponse.setFinishAddress(ride.getFinishAddress());
        rideResponse.setPassengerId(ride.getPassengerId());
        rideResponse.setStartAddress(ride.getStartAddress());
        rideResponse.setOrderDateTime(ride.getOrderDateTime());

        return rideResponse;
    }

    public static List<RideResponse> toDtoList(List<Ride> rides) {
        return rides.stream().map(RideMapper::toDto).collect(Collectors.toList());
    }

    public static void toEntity(RideRequest rideRequest, Ride ride) {
        ride.setDriverId(rideRequest.getDriverId());
        ride.setPrice(rideRequest.getPrice());
//        ride.setStatus(rideRequest.getStatus());
        ride.setFinishAddress(rideRequest.getFinishAddress());
        ride.setPassengerId(rideRequest.getPassengerId());
        ride.setStartAddress(rideRequest.getStartAddress());
        ride.setOrderDateTime(rideRequest.getOrderDateTime());
    }
}
