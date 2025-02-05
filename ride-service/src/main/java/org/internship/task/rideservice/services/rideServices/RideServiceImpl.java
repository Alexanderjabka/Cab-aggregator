package org.internship.task.rideservice.services.rideServices;

import static org.internship.task.rideservice.util.constantMessages.exceptionMessages.RideExceptionMessages.RIDE_ALREADY_EXISTS;
import static org.internship.task.rideservice.util.constantMessages.exceptionMessages.RideExceptionMessages.RIDE_IS_CANCELLED_OR_COMPLETED;
import static org.internship.task.rideservice.util.constantMessages.exceptionMessages.RideExceptionMessages.RIDE_NOT_FOUND_BY_RIDE_ID;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.internship.task.rideservice.dto.RideRequest;
import org.internship.task.rideservice.dto.RideResponse;
import org.internship.task.rideservice.entities.Ride;
import org.internship.task.rideservice.enums.Status;
import org.internship.task.rideservice.exceptions.rideExceptions.InvalidRideOperationException;
import org.internship.task.rideservice.exceptions.rideExceptions.RideNotFoundException;
import org.internship.task.rideservice.mappers.RideMapper;
import org.internship.task.rideservice.repositories.RideRepository;
import org.internship.task.rideservice.services.mapServices.MapService;
import org.internship.task.rideservice.services.priceServices.PriceServiceImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {
    private final RideRepository rideRepository;
    private final MapService mapService;

    @Override
    public List<RideResponse> getAllRides() {
        List<Ride> rides = rideRepository.findAll();
        return RideMapper.toDtoList(rides);
    }

    @Override
    public List<RideResponse> getAllRidesByStatus(Status status) {
        List<Ride> rides = rideRepository.findAllByStatus(status);
        return RideMapper.toDtoList(rides);
    }

    @Override
    public RideResponse getRideById(Long id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(RIDE_NOT_FOUND_BY_RIDE_ID + id));
        return RideMapper.toDto(ride);
    }

    @Override
    public RideResponse createRide(RideRequest rideRequest) {
        if (rideRepository.findByPassengerId(rideRequest.getPassengerId()).isPresent()
                || rideRepository.findByDriverId(rideRequest.getDriverId()).isPresent()) {
            throw new InvalidRideOperationException(RIDE_ALREADY_EXISTS);
        }
        Ride ride = RideMapper.toEntity(rideRequest);

        ride.setPrice(PriceServiceImpl.setPriceForTheRide(mapService.getDistance(rideRequest.getStartAddress(),
                rideRequest.getFinishAddress())));

        ride.setStatus(Status.CREATED);

        rideRepository.save(ride);
        return RideMapper.toDto(ride);
    }

    @Override
    public RideResponse updateRide(Long id, RideRequest rideRequest) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(RIDE_NOT_FOUND_BY_RIDE_ID + id));

        if (ride.getStatus().equals(Status.CANCELLED) || ride.getStatus().equals(Status.COMPLETED)) {
            throw new InvalidRideOperationException(RIDE_IS_CANCELLED_OR_COMPLETED  + ride.getStatus());
        }

        if (rideRepository.findByPassengerId(rideRequest.getPassengerId()).isPresent()
                || rideRepository.findByDriverId(rideRequest.getDriverId()).isPresent()) {
            throw new InvalidRideOperationException(RIDE_ALREADY_EXISTS);
        }

        RideMapper.toEntity(rideRequest, ride);

        ride.setPrice(PriceServiceImpl.setPriceForTheRide(mapService.getDistance(rideRequest.getStartAddress(),
                rideRequest.getFinishAddress())));

        rideRepository.save(ride);

        return RideMapper.toDto(ride);
    }

    @Override
    public RideResponse changeStatus(Long id, Status status) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(RIDE_NOT_FOUND_BY_RIDE_ID + id));

        ride.setStatus(status);
        rideRepository.save(ride);

        return RideMapper.toDto(ride);
    }
}
