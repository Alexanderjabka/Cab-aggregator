package org.internship.task.rideservice.services.rideServices;


import static org.internship.task.rideservice.util.constantMessages.exceptionMessages.RideExceptionMessages.RIDE_MEMBERS_ALREADY_HAVE_RIDES;
import static org.internship.task.rideservice.util.constantMessages.exceptionMessages.RideExceptionMessages.RIDE_NOT_FOUND_BY_RIDE_ID;
import static org.internship.task.rideservice.util.constantMessages.exceptionMessages.RideExceptionMessages.RIDE_STATUS_IS_INCORRECT;
import static org.internship.task.rideservice.util.constantMessages.exceptionMessages.RideExceptionMessages.THIS_DRIVER_ALREADY_HAS_RIDE;
import static org.internship.task.rideservice.util.constantMessages.exceptionMessages.RideExceptionMessages.THIS_PASSENGER_ALREADY_HAS_RIDE;

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
    private final RideMapper rideMapper;

    @Override
    public List<RideResponse> getAllRides() {
        List<Ride> rides = rideRepository.findAll();
        return rideMapper.toDtoList(rides);
    }

    @Override
    public List<RideResponse> getAllRidesByStatus(Status status) {
        List<Ride> rides = rideRepository.findAllByStatus(status);
        return rideMapper.toDtoList(rides);
    }

    @Override
    public RideResponse getRideById(Long id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(RIDE_NOT_FOUND_BY_RIDE_ID + id));
        return rideMapper.toDto(ride);
    }

    @Override
    public RideResponse createRide(RideRequest rideRequest) {
        List<Status> activeStatuses = Status.getActiveStatuses();

        boolean hasActiveRide = rideRepository
                .existsByPassengerIdAndStatusIn(rideRequest.getPassengerId(), activeStatuses)
                || rideRepository
                .existsByDriverIdAndStatusIn(rideRequest.getDriverId(), activeStatuses);

        if (hasActiveRide) {
            throw new InvalidRideOperationException(RIDE_MEMBERS_ALREADY_HAVE_RIDES);
        }

        Ride ride = rideMapper.toEntity(rideRequest);
        ride.setPrice(PriceServiceImpl.setPriceForTheRide(
                mapService.getDistance(rideRequest.getStartAddress(), rideRequest.getFinishAddress())));
        ride.setStatus(Status.CREATED);

        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Override
    public RideResponse updateRide(Long id, RideRequest rideRequest) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(RIDE_NOT_FOUND_BY_RIDE_ID + id));

        if (ride.getStatus() == Status.CANCELLED || ride.getStatus() == Status.COMPLETED) {
            throw new InvalidRideOperationException(RIDE_STATUS_IS_INCORRECT + ride.getStatus());
        }

        List<Status> activeStatuses = Status.getActiveStatuses();

        boolean isPassengerChanged = !ride.getPassengerId().equals(rideRequest.getPassengerId());
        boolean isDriverChanged = !ride.getDriverId().equals(rideRequest.getDriverId());

        if (isPassengerChanged) {
            boolean hasActiveRide = rideRepository.existsByPassengerIdAndStatusIn(
                    rideRequest.getPassengerId(), activeStatuses);
            if (hasActiveRide) {
                throw new InvalidRideOperationException(THIS_PASSENGER_ALREADY_HAS_RIDE + rideRequest.getPassengerId());
            }
            ride.setPassengerId(rideRequest.getPassengerId());
        }

        if (isDriverChanged) {
            boolean hasActiveRide = rideRepository.existsByDriverIdAndStatusIn(
                    rideRequest.getDriverId(), activeStatuses);
            if (hasActiveRide) {
                throw new InvalidRideOperationException(THIS_DRIVER_ALREADY_HAS_RIDE + rideRequest.getDriverId());
            }
            ride.setDriverId(rideRequest.getDriverId());
        }

        ride.setStartAddress(rideRequest.getStartAddress());
        ride.setFinishAddress(rideRequest.getFinishAddress());
        ride.setPrice(PriceServiceImpl.setPriceForTheRide(
                mapService.getDistance(rideRequest.getStartAddress(), rideRequest.getFinishAddress())));

        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Override
    public RideResponse changeStatus(Long id, Status status) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(RIDE_NOT_FOUND_BY_RIDE_ID + id));

        if (ride.getStatus().equals(Status.CANCELLED) || ride.getStatus().equals(Status.COMPLETED)) {
            throw new InvalidRideOperationException(RIDE_STATUS_IS_INCORRECT + ride.getStatus());
        }

        ride.setStatus(status);
        rideRepository.save(ride);

        return rideMapper.toDto(ride);
    }
}
