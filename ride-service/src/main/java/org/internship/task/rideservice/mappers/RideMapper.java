package org.internship.task.rideservice.mappers;

import java.util.List;
import org.internship.task.rideservice.dto.RideRequest;
import org.internship.task.rideservice.dto.RideResponse;
import org.internship.task.rideservice.entities.Ride;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface RideMapper {

    RideResponse toDto(Ride ride);

    @Mapping(target = "price", ignore = true)
    Ride toEntity(RideRequest rideRequest);

    List<RideResponse> toDtoList(List<Ride> rides);
}
