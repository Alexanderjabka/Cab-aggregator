package org.internship.task.driverservice.mappers;

import java.util.List;
import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.dto.cars.CarResponse;
import org.internship.task.driverservice.entities.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "driver", ignore = true)
    Car toEntity(CarRequest carRequest);

    @Mapping(target = "driverId", source = "driver.id")
    CarResponse toDto(Car car);

    List<CarResponse> toDtoList(List<Car> cars);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "driver", ignore = true)
    void updateEntity(@MappingTarget Car car, CarRequest carRequest);
}
