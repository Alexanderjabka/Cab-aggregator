package org.internship.task.driverservice.services.serviceInterfaces;

import org.internship.task.driverservice.dto.drivers.DriverListResponse;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;
import org.springframework.http.ResponseEntity;

public interface DriverService {
    ResponseEntity<DriverListResponse> getAllDrivers();

    ResponseEntity<DriverListResponse> getAllDriversByStatus(boolean status);

    DriverResponse getDriverById(Long id);

    DriverResponse createDriver(DriverRequest driverRequest);

    DriverResponse updateDriver(String email, DriverRequest driverRequest);

    DriverResponse getFirstFreeDriverAndChangeStatus();

    void releaseDriver(Long driverId);

    void deleteDriver(Long id);
}
