package org.internship.task.driverservice.mappers;

import java.util.List;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
import org.internship.task.driverservice.entities.Car;
import org.internship.task.driverservice.entities.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "cars", ignore = true)
    Driver toEntity(DriverRequest driverRequest);

    @Mapping(target = "carId", source = "cars", qualifiedByName = "mapCarIds")
    DriverResponse toDto(Driver driver);

    List<DriverResponse> toDtoList(List<Driver> drivers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "cars", ignore = true)
    void updateEntity(@MappingTarget Driver driver, DriverRequest driverRequest);

    @Named("mapCarIds")
    static List<Long> mapCarIds(List<Car> cars) {
        if (cars == null) {
            return List.of();
        }

        return cars.stream()
                .filter(car -> !Boolean.TRUE.equals(car.getIsDeleted()))
                .map(Car::getId)
                .toList();
    }
}
