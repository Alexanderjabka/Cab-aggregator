package org.internship.task.passengerservice.mappers;

import org.internship.task.passengerservice.dto.PassengerRequest;
import org.internship.task.passengerservice.dto.PassengerResponse;
import org.internship.task.passengerservice.entities.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PassengerMapper {
    Passenger toEntity(PassengerRequest passengerRequest);

    PassengerResponse toDto(Passenger passenger);

    void updateEntity(@MappingTarget Passenger passenger, PassengerRequest passengerRequest);

    List<PassengerResponse> toDtoList(List<Passenger> passengers);
}
