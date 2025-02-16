package org.internship.task.rideservice.services.rideServices;

import static org.internship.task.rideservice.util.constantMessages.exceptionMessages.RideExceptionMessages.RIDE_NOT_FOUND_BY_RIDE_ID;
import static org.internship.task.rideservice.util.constantMessages.exceptionMessages.RideExceptionMessages.RIDE_STATUS_IS_INCORRECT;
import static org.internship.task.rideservice.util.constantMessages.exceptionMessages.RideExceptionMessages.THIS_DRIVER_ALREADY_HAS_RIDE;
import static org.internship.task.rideservice.util.constantMessages.exceptionMessages.RideExceptionMessages.THIS_PASSENGER_ALREADY_HAS_RIDE;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.internship.task.rideservice.clients.DriverClient;
import org.internship.task.rideservice.dto.AssignDriverResponse;
import org.internship.task.rideservice.dto.RideListResponse;
import org.internship.task.rideservice.dto.RideRequest;
import org.internship.task.rideservice.dto.RideResponse;
import org.internship.task.rideservice.dto.StatusRequest;
import org.internship.task.rideservice.entities.Ride;
import org.internship.task.rideservice.enums.Status;
import org.internship.task.rideservice.exceptions.rideExceptions.InvalidRideOperationException;
import org.internship.task.rideservice.exceptions.rideExceptions.RideNotFoundException;
import org.internship.task.rideservice.mappers.RideMapper;
import org.internship.task.rideservice.repositories.RideRepository;
import org.internship.task.rideservice.services.mapServices.MapService;
import org.internship.task.rideservice.services.priceServices.PriceServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final MapService mapService;
    private final RideMapper rideMapper;
    private final DriverClient driverClient;

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<RideListResponse> getAllRides() {
        List<Ride> rides = rideRepository.findAllByOrderByIdAsc();

        return rides.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(RideListResponse.builder()
                .rides(rideMapper.toDtoList(rides))
                .build());
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<RideListResponse> getAllRidesByStatus(Status status) {
        List<Ride> rides = rideRepository.findAllByStatusOrderByIdAsc(status);

        return rides.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(RideListResponse.builder()
                .rides(rideMapper.toDtoList(rides))
                .build());
    }

    @Transactional(readOnly = true)
    @Override
    public RideResponse getRideById(Long id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(RIDE_NOT_FOUND_BY_RIDE_ID + id));
        return rideMapper.toDto(ride);
    }

    @Transactional
    @Override
    public RideResponse createRide(RideRequest rideRequest) {
        if (rideRepository.existsByPassengerIdAndStatusIn(rideRequest.getPassengerId(), Status.getActiveStatuses())) {
            throw new InvalidRideOperationException(THIS_PASSENGER_ALREADY_HAS_RIDE);
        }

        AssignDriverResponse assignDriverResponse = driverClient.assignDriver();
        if (assignDriverResponse == null || assignDriverResponse.getDriverId() == null) {
            throw new InvalidRideOperationException("No available drivers at the moment.");
        }

        Ride ride = rideMapper.toEntity(rideRequest);
        ride.setDriverId(assignDriverResponse.getDriverId());
        ride.setPrice(PriceServiceImpl.setPriceForTheRide(
                mapService.getDistance(rideRequest.getStartAddress(), rideRequest.getFinishAddress())));
        ride.setStatus(Status.CREATED);

        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Transactional
    @Override
    public RideResponse updateRide(Long id, RideRequest rideRequest) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(RIDE_NOT_FOUND_BY_RIDE_ID + id));

        if (ride.getStatus() == Status.CANCELLED || ride.getStatus() == Status.COMPLETED) {
            throw new InvalidRideOperationException(RIDE_STATUS_IS_INCORRECT + ride.getStatus());
        }

        if (!ride.getPassengerId().equals(rideRequest.getPassengerId())) {
            if (rideRepository.existsByPassengerIdAndStatusIn(rideRequest.getPassengerId(), Status.getActiveStatuses())) {
                throw new InvalidRideOperationException(THIS_PASSENGER_ALREADY_HAS_RIDE + rideRequest.getPassengerId());
            }
            ride.setPassengerId(rideRequest.getPassengerId());
        }

        ride.setStartAddress(rideRequest.getStartAddress());
        ride.setFinishAddress(rideRequest.getFinishAddress());
        ride.setPrice(PriceServiceImpl.setPriceForTheRide(
                mapService.getDistance(rideRequest.getStartAddress(), rideRequest.getFinishAddress())));

        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Transactional
    @Override
    public RideResponse changeStatus(Long id, StatusRequest status) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(RIDE_NOT_FOUND_BY_RIDE_ID + id));

        if (ride.getStatus().equals(Status.CANCELLED) || ride.getStatus().equals(Status.COMPLETED)) {
            throw new InvalidRideOperationException(RIDE_STATUS_IS_INCORRECT + ride.getStatus());
        }

        ride.setStatus(status.getStatus());
        rideRepository.save(ride);

        if (status.getStatus() == Status.CANCELLED || status.getStatus() == Status.COMPLETED) {
            driverClient.releaseDriver(ride.getDriverId());
        }

        return rideMapper.toDto(ride);
    }

}
