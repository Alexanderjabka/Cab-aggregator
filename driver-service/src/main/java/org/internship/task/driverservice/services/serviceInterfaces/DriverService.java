package org.internship.task.driverservice.services.serviceInterfaces;

import java.util.List;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.dto.drivers.DriverResponse;

public interface DriverService {
    List<DriverResponse> getAllDrivers();

    List<DriverResponse> getAllDriversByStatus(boolean status);

    DriverResponse getDriverById(Long id);

    DriverResponse createDriver(DriverRequest driverRequest);

    DriverResponse updateDriver(String email, DriverRequest driverRequest);

    void deleteDriver(Long id);
}
